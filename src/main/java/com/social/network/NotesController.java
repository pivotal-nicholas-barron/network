package com.social.network;

import com.social.network.models.Friend;
import com.social.network.models.Note;
import com.social.network.services.NoteService;
import com.social.network.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    NoteService noteService;

    @Autowired
    UserService userService;

    @GetMapping("/{friendEmail}")
    public String setModelOfNotesForUser(@PathVariable String friendEmail, Model model, Principal principal) {
        String currentUsersEmail = principal.getName();
        Friend friendPair = userService.findFriendMatchingEmailPair(currentUsersEmail, friendEmail);
        if (!friendEmail.equals(currentUsersEmail) && (friendPair == null || friendPair.getPending())) {
            return "redirect:/";
        }
        ArrayList<Note> notes = noteService.findNotesForUser(friendEmail);
        model.addAttribute("notes", notes);
        model.addAttribute("user", friendEmail);
        return "notes";
    }

    @GetMapping("/{friendEmail}/{noteId}")
    public String directToCommentView(@PathVariable String friendEmail, Principal principal) {
        String currentUsersEmail = principal.getName();
        Friend friendPair = userService.findFriendMatchingEmailPair(currentUsersEmail, friendEmail);
        if (!friendEmail.equals(currentUsersEmail) && (friendPair == null || friendPair.getPending())) {
            return "redirect:/";
        }
        return "index";
    }
}
