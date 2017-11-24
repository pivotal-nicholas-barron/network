package com.social.network;

import com.social.network.models.Note;
import com.social.network.services.NoteService;
import com.social.network.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @GetMapping("/")
    public String welcome(Map<String, Object> model, Principal principal) {
        String currentUsersEmail = principal.getName();
        model.put("userInfo", userService.getAllFriendsForUser(currentUsersEmail));
        model.put("currentUser", currentUsersEmail);
        return "home";
    }

    @PostMapping("/acceptpending")
    public String accept(HttpServletRequest request) {
        userService.acceptPendingRequest(request.getUserPrincipal().getName(), request.getParameter("friendEmail"));
        return "redirect:/";
    }

    @PostMapping("/rejectpending")
    public String reject(HttpServletRequest request) {
        userService.rejectPendingRequest(request.getUserPrincipal().getName(), request.getParameter("friendEmail"));
        return "redirect:/";
    }

    @PostMapping("/savenote")
    public String savenote(HttpServletRequest request) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Date date = new Date();
        try {
            date = formatter.parse(request.getParameter("datetime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Note note = new Note();
        note.setContent(request.getParameter("content"));
        note.setUsername(request.getUserPrincipal().getName());
        note.setTimestamp(date.getTime());

        noteService.saveNote(note);
        return "redirect:/";
    }
}
