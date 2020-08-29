package com.aramirez.redditclone.repository;

import com.aramirez.redditclone.model.Comment;
import com.aramirez.redditclone.model.Post;
import com.aramirez.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);
}
