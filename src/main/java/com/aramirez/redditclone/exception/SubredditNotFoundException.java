package com.aramirez.redditclone.exception;

public class SubredditNotFoundException extends RuntimeException {
    public SubredditNotFoundException(String message) {
        super("Subreddit " + message + "not found");
    }
}
