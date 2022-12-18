package com.example.messanger;

public class User {
    private String username;
    private String Email;
    private String ProfilePicture;

    public User ()
    {}

    public User(String username, String email, String profilePicture) {
        this.username = username;
        Email = email;
        ProfilePicture = profilePicture;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }
}
