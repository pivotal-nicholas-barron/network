package com.social.network;

import com.social.network.configuration.WebSecurityConfiguration;
import com.social.network.models.User;
import com.social.network.services.NoteService;
import com.social.network.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(CreateUserController.class)
@Import(WebSecurityConfiguration.class)
public class CreateUserTest {


    @Configuration
    static class config {

        @Bean
        UserService sevice() {

        }
    }

    @MockBean
    UserService userService;

    @MockBean
    NoteService noteService;

    @Autowired
    MockMvc mockMvc;

    @Qualifier("dataSource")
    @MockBean
    DataSource dataSource;

    @Test
    public void createAccountPath() throws Exception {
        mockMvc.perform(get("/createuser")).andDo(print())
                .andExpect(view().name(is("create_user")));
    }

    @Test
    public void whenUserSubmitsCredentials_AccountIsCreatedAndUserIsShownHomeView() throws Exception {
        User setUser = new User();
        setUser.setUsername("newUser");
        setUser.setPassword("newPass");
        setUser.setUsername("Email@email.email");

        when(userService.addUser(setUser)).thenReturn(setUser);

        mockMvc.perform(post("/createuser")
                .param("username", setUser.getUsername())
                .param("password", setUser.getPassword())
                .with((csrf()))).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void whenUserSubmitsExistingCredentialsInCreateAccount_UserIsStillOnCreateAccountView() throws Exception {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("newPass");
        user.setUsername("Email@email.email");

        when(userService.addUser(user)).thenReturn(null);

        MvcResult result = mockMvc.perform(post("/createuser")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .with((csrf()))).andDo(print())
                .andExpect(view().name("create_user"))
                .andReturn();

        assertThat(result.getRequest().getRequestURI()).isEqualTo("/createuser");
    }
}
