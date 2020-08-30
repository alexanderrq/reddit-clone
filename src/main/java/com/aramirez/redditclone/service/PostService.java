package com.aramirez.redditclone.service;

import com.aramirez.redditclone.dto.PostRequest;
import com.aramirez.redditclone.dto.PostResponse;
import com.aramirez.redditclone.exception.PostNotFoundException;
import com.aramirez.redditclone.exception.SubredditNotFoundException;
import com.aramirez.redditclone.mapper.PostMapper;
import com.aramirez.redditclone.model.Post;
import com.aramirez.redditclone.model.Subreddit;
import com.aramirez.redditclone.model.User;
import com.aramirez.redditclone.repository.PostRepository;
import com.aramirez.redditclone.repository.SubredditRepository;
import com.aramirez.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final AuthService authService;

    public PostResponse save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        Post post = postRepository.save(postMapper.mapDtoToPost(postRequest,
                subreddit, authService.getCurrentUser()));

        return postMapper.mapPostToDto(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapPostToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapPostToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));

        return postRepository.findAllBySubreddit(subreddit)
                .stream()
                .map(postMapper::mapPostToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapPostToDto)
                .collect(Collectors.toList());
    }
}
