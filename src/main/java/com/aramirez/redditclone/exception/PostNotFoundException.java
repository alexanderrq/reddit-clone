package com.aramirez.redditclone.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super("Post with id - " + message + "not found");
    }
}
