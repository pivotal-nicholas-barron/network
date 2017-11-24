package com.social.network;

import com.social.network.models.User;
import com.social.network.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AddFriendController {

    @Autowired
    UserService service;

    @GetMapping("/addfriend")
    public String redirectToAddFiendView(Map<String, Object> model) {
        return "add_friend";
    }

    @PostMapping("/addfriend")
    public String addFriendToUsersListAndRedirectToHome(Map<String, Object> model, HttpServletRequest request) {
        String userEmail = request.getUserPrincipal().getName();
        String friendEmail = request.getParameter("friendemail");

        User currentUser = service.getUserForUsername(userEmail);
        if(service.addFriendToUser(currentUser, friendEmail) == null) {
            model.put("error", "bad username");
            return "add_friend";
        }
        return "redirect:/";
    }
}
