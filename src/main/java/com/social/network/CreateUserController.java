package com.social.network;

import com.social.network.models.User;
import com.social.network.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class CreateUserController {

    @Autowired
    UserService userService;

    @GetMapping("/createuser")
    public String redirectToCreateUser(Map<String, Object> model) {
        return "create_user";
    }

    @PostMapping("/createuser")
        public ModelAndView submitNewUserToUserService(HttpServletRequest request) {
        String emailAddress = request.getParameter("username");
        String password = request.getParameter("password");

        User user = new User();
        user.setUsername(emailAddress);
        user.setPassword(password);
        user.setEnabled(1);
        user.setRole("USER");

        if(userService.addUser(user) == null) {
            ModelAndView createUserViewAndModel = new ModelAndView("create_user");
            createUserViewAndModel.addObject("error", "username");
            return createUserViewAndModel;
        }
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        request.setAttribute("username", emailAddress);
        request.setAttribute("password", password);
        return new ModelAndView("redirect:/login");
    }
}
