package com.aramirez.redditclone.repository;

import com.aramirez.redditclone.model.Post;
import com.aramirez.redditclone.model.Subreddit;
import com.aramirez.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}
