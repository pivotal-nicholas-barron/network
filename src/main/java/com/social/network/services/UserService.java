package com.social.network.services;

import com.social.network.models.Friend;
import com.social.network.models.FriendRepository;
import com.social.network.models.User;
import com.social.network.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    public ArrayList<Friend> getAllFriendsForUser(String email) {
        ArrayList<Friend> listOfFriends = new ArrayList<>();
        for (Friend friend: friendRepository.findAll()) {
            if(friend.getEmail().equals(email) || friend.getFriendEmail().equals(email)){
                listOfFriends.add(friend);
            }
        }
        return listOfFriends;
    }


    public User addUser(User setUser) {
        if (!userRepository.existsById(setUser.getUsername())) {
            return userRepository.save(setUser);
        }
        return null;
    }

    public Friend addFriendToUser(User user, String friendEmail) {
        if (!userRepository.existsById(friendEmail) || user.getUsername().equals(friendEmail)) {
            return null;
        }
        for(Friend friend: friendRepository.findAll()) {
            if(friend.getFriendEmail().equals(user.getUsername())) {
                return null;
            }
        }
        Friend friend = new Friend();
        friend.setEmail(user.getUsername());
        friend.setFriendEmail(friendEmail);
        friend.setPending(true);
        return friendRepository.save(friend);
    }

    public User getUserForUsername(String userEmail) {
        return userRepository.findById(userEmail).get();
    }

    public ArrayList<Friend> getPendingFriendsForUser(String email) {
        ArrayList<Friend> pendingFriends = new ArrayList<>();
        for(Friend friend: getAllFriendsForUser(email)) {
            if(friend.getPending()){
                pendingFriends.add(friend);
            }
        }
        return pendingFriends;
    }

    public Friend acceptPendingRequest(String email, String friendEmail) {
        Friend friend = findFriendMatchingEmailPair(email, friendEmail);
        if (friend == null) {
            return null;
        }
        friend.setPending(false);
        return friendRepository.save(friend);
    }

    public void rejectPendingRequest(String email, String friendEmail) {
        Friend friend = findFriendMatchingEmailPair(email, friendEmail);
        if(friend != null) {
            friendRepository.delete(friend);
        }
    }

    public Friend findFriendMatchingEmailPair(String email, String friendEmail) {
        for (Friend friend: friendRepository.findAll()) {
            if ((friend.getEmail().equals(email) && friend.getFriendEmail().equals(friendEmail))
                    || (friend.getEmail().equals(friendEmail)) && friend.getFriendEmail().equals(email)) {
                return friend;
            }
        }
        return null;
    }
}
