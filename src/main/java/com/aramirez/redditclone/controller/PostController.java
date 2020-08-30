package com.aramirez.redditclone.controller;

import com.aramirez.redditclone.dto.PostRequest;
import com.aramirez.redditclone.dto.PostResponse;
import com.aramirez.redditclone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.save(postRequest));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getAll());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getPost(postId));
    }

    @GetMapping("/by-subreddit/{subredditId}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long subredditId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getPostsBySubreddit(subredditId));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getPostsByUsername(username));
    }
}
