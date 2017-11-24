package com.social.network;

import com.social.network.models.Note;
import com.social.network.models.NoteRepository;
import com.social.network.services.NoteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteServiceTest {

    @MockBean
    NoteRepository noteRepository;

    @Autowired
    NoteService service;

    @Test
    public void givenUserId_AllNotesAreReturnedForUser() {
        ArrayList<Note> notes = new ArrayList<>();
        Note inNote = new Note();
        inNote.setUsername("expecteduser@pivotal.io");
        inNote.setContent("");
        inNote.setTimestamp(System.currentTimeMillis());
        notes.add(inNote);
        Note outNote = new Note();
        outNote.setUsername("unexpecteduser@pivotal.io");
        outNote.setContent("");
        outNote.setTimestamp(System.currentTimeMillis());
        notes.add(outNote);
        when(noteRepository.findAll()).thenReturn(notes);

        assertThat(service.findNotesForUser("expecteduser@pivotal.io")).contains(inNote);
        assertThat(service.findNotesForUser("expecteduser@pivotal.io")).doesNotContain(outNote);
    }

}
