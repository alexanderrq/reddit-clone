package com.aramirez.redditclone.service;

import com.aramirez.redditclone.dto.VoteDto;
import com.aramirez.redditclone.exception.PostNotFoundException;
import com.aramirez.redditclone.exception.RedditException;
import com.aramirez.redditclone.model.Post;
import com.aramirez.redditclone.model.Vote;
import com.aramirez.redditclone.repository.PostRepository;
import com.aramirez.redditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.aramirez.redditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(voteDto.getPostId().toString()));
        Optional<Vote> voteByPostAndUser = voteRepository
                .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent()
                && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new RedditException("You have already " + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapDtoToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapDtoToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
