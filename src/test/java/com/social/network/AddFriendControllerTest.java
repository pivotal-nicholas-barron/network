package com.social.network;

import com.social.network.models.Friend;
import com.social.network.models.User;
import com.social.network.services.CommentService;
import com.social.network.services.NoteService;
import com.social.network.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest
public class AddFriendControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    NoteService noteService;

    @MockBean
    CommentService commentService;

    @Test
    @WithMockUser
    public void whenUserIsAuthed_AddFriendPageIsAccessible() throws Exception {
        mockMvc.perform(get("/addfriend")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add_friend"));
    }

    @Test
    @WithMockUser("Email@email.email")
    public void whenUserAddsAFriend_FriendIsShownOnHomeView() throws Exception {
        User setUser = new User();
        setUser.setPassword("newPass");
        setUser.setUsername("Email@email.email");

        Friend newFriend = new Friend();
        newFriend.setFriendEmail("otherUser@email.email");
        newFriend.setEmail(setUser.getUsername());

        when(userService.addFriendToUser(setUser, "otherUser@email.email")).thenReturn(newFriend);
        when(userService.getUserForUsername(setUser.getUsername())).thenReturn(setUser);

        mockMvc.perform(post("/addfriend")
                .param("friendemail", newFriend.getFriendEmail())
                .with((csrf()))).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(userService, times(1)).addFriendToUser(setUser, "otherUser@email.email");
    }

    @Test
    @WithMockUser("Email@email.com")
    public void whenUserAddsAFriendThatAlreadyExists_RemainOnAddUserViewWithError() throws Exception {
        User setUser = new User();
        setUser.setPassword("newPass");
        setUser.setUsername("Email@email.email");

        Friend newFriend = new Friend();
        newFriend.setFriendEmail("otherUser@email.email");
        newFriend.setEmail(setUser.getUsername());

        when(userService.addFriendToUser(setUser, "otherUser@email.email")).thenReturn(null);
        when(userService.getUserForUsername(setUser.getUsername())).thenReturn(setUser);

        MvcResult result =  mockMvc.perform(post("/addfriend")
                .param("friendemail", newFriend.getFriendEmail())
                .with((csrf()))).andDo(print())
                .andExpect(view().name("add_friend"))
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains("User is invalid");
    }
}
