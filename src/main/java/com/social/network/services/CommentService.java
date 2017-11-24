package com.social.network.services;

import com.social.network.models.Comment;
import com.social.network.models.CommentRepository;
import com.social.network.models.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    NoteRepository noteRepository;

    public HashMap<String, Object> getCommentsForNote(Integer noteId) {
        ArrayList<Comment> comments = new ArrayList<>();

        new ArrayList<>((Collection<Comment>) commentRepository.findAll()).stream()
                .filter(element -> noteId.equals(element.getNoteId()))
                .forEach(comments::add);

        HashMap<String,Object> keyValPairSetup = new HashMap<>();
        keyValPairSetup.put("name", noteRepository.findById(noteId).get().getContent());
        keyValPairSetup.put("comments", comments);
        return keyValPairSetup;
    }

    public Comment saveComment(Comment requestBody) {
        return commentRepository.save(requestBody);
    }
}
