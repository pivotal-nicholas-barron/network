package com.social.network.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.Format;
import java.text.SimpleDateFormat;

@Entity
public class Note {

    @Id
    private int noteId;

    private String username;
    private long timestamp;
    private String content;

    public int getId() {
        return noteId;
    }

    public void setId(Integer noteId) {
        this.noteId = noteId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestampAsDateTime() {
        Format format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        return format.format(getTimestamp());
    }
}
