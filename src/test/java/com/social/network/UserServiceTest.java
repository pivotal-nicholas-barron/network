package com.social.network;

import com.social.network.models.Friend;
import com.social.network.models.FriendRepository;
import com.social.network.models.User;
import com.social.network.models.UserRepository;
import com.social.network.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    FriendRepository friendRepository;

    @Autowired
    UserService service;

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Test
    public void retrieveFriendsListWillReturnListOfFriendsForAUser() throws Exception {
        when(userRepository.findAll()).thenReturn(generateListOfUsers());
        when(friendRepository.findAll()).thenReturn(generateListOfFriends());

        Friend friend = new Friend();
        friend.setFriendEmail("john@kahin.io");
        friend.setEmail("kahin@pivotal.io");

        assertThat(service.getAllFriendsForUser("kahin@pivotal.io").contains(friend)).isEqualTo(true);
        assertThat(service.getAllFriendsForUser("kahin@pivotal.io").size()).isEqualTo(1);
    }

    @Test
    public void retrievePendingListOfFriendRequestsForAUser() throws Exception {
        when(userRepository.findAll()).thenReturn(generateListOfUsers());
        when(friendRepository.findAll()).thenReturn(generateListOfPendingFriendRequests());

        Friend pendingFriend = new Friend();
        pendingFriend.setEmail("kahin@pivotal.io");
        pendingFriend.setFriendEmail("john@kahin.io");

        Friend friend = new Friend();
        friend.setEmail("kahin@pivotal.io");
        friend.setFriendEmail("notkahin@kahin.io");

        assertThat(service.getPendingFriendsForUser("kahin@pivotal.io").contains(pendingFriend)).isEqualTo(true);
        assertThat(service.getPendingFriendsForUser("kahin@pivotal.io").contains(friend)).isEqualTo(false);
    }

    @Test
    public void createUserWillInteractWithUserRepoSuccessfullyAndReturnAReferenceToNewUser() throws Exception {
        User setUser = getPlainUser();
        User spyUser = getSpyUser();
        when(userRepository.save(setUser)).thenReturn(spyUser);

        User result = service.addUser(setUser);

        assertThat(result).isEqualTo(spyUser);
    }

    @Test
    public void createAccountWillFailIfExistingCredentialsAreUsed() {
        User user = getPlainUser();
        when(userRepository.exists(user.getUsername())).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        User result = service.addUser(user);

        assertThat(result).isNull();
    }

    @Test
    public void whenAnExistingUserIsAddedToAFriendsList_TheNewUserIsReturned() {
        User setUser = getPlainUser();
        User spyUser = getSpyUser();

        Friend friend = new Friend();
        friend.setEmail(setUser.getUsername());
        friend.setFriendEmail(spyUser.getUsername());

        Friend spyFriend = new Friend();
        spyFriend.setEmail(setUser.getUsername());
        spyFriend.setFriendEmail(spyUser.getUsername());

        when(userRepository.exists(friend.getFriendEmail())).thenReturn(true);
        when(friendRepository.save(friend)).thenReturn(spyFriend);
        when(friendRepository.findAll()).thenReturn(new ArrayList<>());

        Friend friendResult = service.addFriendToUser(setUser, friend.getFriendEmail());

        assertThat(friendResult).isEqualTo(spyFriend);
    }

    @Test
    public void whenAUserIsAddedToAFriendsListThatDoesNotExist_UserIsNotAddedAndNullIsReturned() {
        User setUser = getPlainUser();
        User spyUser = getSpyUser();

        Friend friend = new Friend();
        friend.setEmail(setUser.getUsername());
        friend.setFriendEmail(spyUser.getUsername());

        Friend spyFriend = new Friend();
        friend.setEmail(setUser.getUsername());
        friend.setFriendEmail(spyUser.getUsername());

        when(userRepository.exists(friend.getFriendEmail())).thenReturn(false);
        when(friendRepository.save(friend)).thenReturn(spyFriend);

        Friend friendResult = service.addFriendToUser(setUser, friend.getFriendEmail());

        assertThat(friendResult).isNull();
    }

    @Test
    public void whenAUserTriesToAddThemselvesToTheirOwnFriendsList_ANullValueIsReturned() {
        User currentUser = getPlainUser();

        Friend friend = new Friend();
        friend.setEmail(currentUser.getUsername());
        friend.setFriendEmail(currentUser.getUsername());

        when(userRepository.exists(friend.getFriendEmail())).thenReturn(true);
        when(friendRepository.save(friend)).thenReturn(friend);

        Friend friendResult = service.addFriendToUser(currentUser, "Email@email.email");
        assertThat(friendResult).isNull();
    }

    @Test
    public void whenFriendsListIsGenerated_FullBidirectionalListIsReturned() {
        ArrayList<Friend> friends = generateListOfFriends();

        Friend reverseFriend = new Friend();
        reverseFriend.setEmail("john@kahin.io");
        reverseFriend.setFriendEmail("kahin@pivotal.io");

        friends.add(reverseFriend);

        when(friendRepository.findAll()).thenReturn(friends);

        assertThat(service.getAllFriendsForUser("kahin@pivotal.io")).contains(reverseFriend);
    }

    @Test
    public void whenFriendCombinationExists_AddingUserReturnsNull() {
        User currentUser = getPlainUser();
        ArrayList<Friend> friends = new ArrayList<>();

        Friend reverseFriend = new Friend();
        reverseFriend.setEmail("john@kahin.io");
        reverseFriend.setFriendEmail("Email@email.email");

        Friend friend = new Friend();
        friend.setEmail("Email@email.email");
        friend.setFriendEmail("john@kahin.io");

        friends.add(reverseFriend);

        when(userRepository.exists(friend.getFriendEmail())).thenReturn(true);
        when(friendRepository.save(friend)).thenReturn(friend); // this is to check when it fails
        when(friendRepository.findAll()).thenReturn(friends);

        assertThat(service.addFriendToUser(currentUser, "john@kahin.io")).isNull();
    }

    @Test
    public void whenUserAcceptsAFriendRequest_PendingStatusIsUpdatedForFriend() {
        ArrayList<Friend> friends = new ArrayList<>();
        Friend pendingFriend = new Friend();
        pendingFriend.setEmail("john@kahin.io");
        pendingFriend.setFriendEmail("Email@email.email");
        pendingFriend.setPending(true);
        friends.add(pendingFriend);

        Friend nonPendingFriend = new Friend();
        nonPendingFriend.setEmail("john@kahin.io");
        nonPendingFriend.setFriendEmail("Email@email.email");
        nonPendingFriend.setPending(false);

        Friend nonPendingFriendSpy = new Friend();
        nonPendingFriendSpy.setEmail("ImASpy");
        nonPendingFriendSpy.setFriendEmail("ImASpy");
        nonPendingFriendSpy.setPending(false);

        when(friendRepository.findAll()).thenReturn(friends);
        when(friendRepository.save(nonPendingFriend)).thenReturn(nonPendingFriendSpy);

        assertThat(service.acceptPendingRequest(pendingFriend.getEmail(), pendingFriend.getFriendEmail())).isEqualTo(nonPendingFriendSpy);
    }

    @Test
    public void whenUserRejectsAFriendRequest_FriendIsRemovedFromDatabase() {
        ArrayList<Friend> friends = new ArrayList<>();
        Friend friend = new Friend();
        friend.setEmail("john@kahin.io");
        friend.setFriendEmail("Email@email.email");
        friend.setPending(true);
        friends.add(friend);

        when(friendRepository.findAll()).thenReturn(friends);

        service.rejectPendingRequest(friend.getEmail(), friend.getFriendEmail());
        verify(friendRepository, times(1)).delete(friend);
    }

    private ArrayList<Friend> generateListOfFriends() {
        ArrayList<Friend> friends = new ArrayList<>();
        Friend friend1 = new Friend();
        friend1.setEmail("kahin@pivotal.io");
        friend1.setFriendEmail("john@kahin.io");
        friends.add(friend1);
        Friend friend2 = new Friend();
        friend2.setEmail("john@pivotal.io");
        friend2.setFriendEmail("notkahin@kahin.io");
        friends.add(friend2);
        return friends;
    }

    private ArrayList<Friend> generateListOfPendingFriendRequests() {
        ArrayList<Friend> friends = new ArrayList<>();
        Friend friend1 = new Friend();
        friend1.setEmail("kahin@pivotal.io");
        friend1.setFriendEmail("john@kahin.io");
        friend1.setPending(true);
        friends.add(friend1);
        Friend friend2 = new Friend();
        friend2.setEmail("kahin@pivotal.io");
        friend2.setFriendEmail("notkahin@kahin.io");
        friend2.setPending(false);
        friends.add(friend2);
        return friends;
    }

    private ArrayList<User> generateListOfUsers() {
        ArrayList<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("kahin@kahin.io");
        users.add(user1);
        User user2 = new User();
        user2.setUsername("notkahin@kahin.io");
        users.add(user2);
        User user3 = new User();
        user3.setUsername("john@kahin.io");
        users.add(user3);
        return users;
    }

    private User getPlainUser() {
        User setUser = new User();
        setUser.setPassword("newPass");
        setUser.setUsername("Email@email.email");
        return setUser;
    }

    private User getSpyUser() {
        User spyUser = new User();
        spyUser.setPassword("newPass");
        spyUser.setUsername("spy@email.email");
        return spyUser;
    }
}
