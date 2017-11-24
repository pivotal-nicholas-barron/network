package com.social.network;


import com.social.network.configuration.WebSecurityConfiguration;
import com.social.network.services.CommentService;
import com.social.network.services.NoteService;
import com.social.network.services.UserService;
import org.hibernate.validator.constraints.ModCheck;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import(WebSecurityConfiguration.class)
public class MvcConfigTest {

    @MockBean
    UserService service;

    @MockBean
    NoteService noteService;

    @MockBean
    CommentService commentService;

    @Autowired
    MockMvc mockMvc;

    @Qualifier("dataSource")
    @MockBean
    DataSource datasource;

    @Test
    public void whenUserIsNotAuthenticated_loginPageIsAccessible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUserIsNotAuthenticated_homePageRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}