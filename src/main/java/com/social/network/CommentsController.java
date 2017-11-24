package com.social.network;

import com.social.network.models.Comment;
import com.social.network.services.CommentService;
import com.social.network.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    CommentService commentService;

    @GetMapping("/{noteId}")
    public @ResponseBody
    HashMap getCommentsForNote(@PathVariable String noteId) {
        return commentService.getCommentsForNote(Integer.parseInt(noteId));
    }

    @PostMapping("/{noteId}")
    public @ResponseBody Comment saveComment(@RequestBody Comment requestBody) {
        return commentService.saveComment(requestBody);
    }
}
