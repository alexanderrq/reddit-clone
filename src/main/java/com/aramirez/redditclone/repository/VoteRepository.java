package com.aramirez.redditclone.repository;

import com.aramirez.redditclone.model.Post;
import com.aramirez.redditclone.model.User;
import com.aramirez.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
