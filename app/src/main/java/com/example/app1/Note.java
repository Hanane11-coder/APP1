package com.example.app1;

public class Note {
    private int id;
    private String title;
    private String content;
    private boolean archived;
    private boolean deleted;
    private byte[] image;

    public Note(int id, String title, String content, boolean archived, boolean deleted, byte[] image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.archived = archived;
        this.deleted = deleted;
        this.image = image;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
