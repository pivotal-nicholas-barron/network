package com.social.network.services;

import com.social.network.models.CommentRepository;
import com.social.network.models.Note;
import com.social.network.models.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.StreamSupport;

@Service
public class NoteService {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    CommentRepository commentRepository;

    public ArrayList<Note> findNotesForUser(String email) {
        ArrayList<Note> notes = new ArrayList<>();

        Comparator c = Comparator.comparing(Note::getContent);

        notes.trimToSize();
        notes.add((notes.size() == 0) ? new Note() : new Note());

        StreamSupport.stream(noteRepository.findAll().spliterator(), false)
                .filter(element -> email.equals(element.getUsername()))
                .forEach(notes::add);

        return notes;
    }

    public Note saveNote(Note note) {
        note.setId(new Random().nextInt());
        System.out.println("This id is: " + note.getId());
        return noteRepository.save(note);
    }
}
