package com.social.network;

import com.social.network.models.Friend;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    NoteService noteService;

    @Test
    @WithMockUser
    public void userCanNavigateToHomeSuccessfully() throws Exception {
        mockMvc.perform(get("/")).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "kahin@pivotal.io")
    public void whenUserNavigatesToHomePage_userDataIsDisplayed() throws Exception {
        when(userService.getAllFriendsForUser(eq("kahin@pivotal.io"))).thenReturn(generateListOfFriends());

        MvcResult result = mockMvc.perform(get("/")).andDo(print())
                .andExpect(view().name(is("home")))
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).contains("john@pivotal.io");
    }

    @Test
    @WithMockUser
    public void whenUserNavigatesToHomePageThatHasNoFriends_userDataIsNotDisplayed() throws Exception {
        when(userService.getAllFriendsForUser("kahin@pivotal.io")).thenReturn(generateListOfFriends());

        MvcResult result = mockMvc.perform(get("/")).andDo(print())
                .andExpect(view().name(is("home")))
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).doesNotContain("john@pivotal.io");
    }

    private ArrayList<Friend> generateListOfFriends() {
        Friend friend = new Friend();
        friend.setEmail("kahin@pivotal.io");
        friend.setFriendEmail("john@pivotal.io");
        friend.setPending(true);

        ArrayList<Friend> friends = new ArrayList<>();
        friends.add(friend);
        return friends;
    }

}
