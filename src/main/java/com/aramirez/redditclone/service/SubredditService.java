package com.aramirez.redditclone.service;

import com.aramirez.redditclone.dto.SubredditDto;
import com.aramirez.redditclone.exception.RedditException;
import com.aramirez.redditclone.mapper.SubredditMapper;
import com.aramirez.redditclone.model.Subreddit;
import com.aramirez.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final SubredditMapper subredditMapper;

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new RedditException("Subreddit not found with id - " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditMapper.mapDtoToSubreddit(subredditDto);
        subreddit.setName("/r/" + subredditDto.getName());
        subreddit.setCreatedDate(Instant.now());
        subreddit.setUser(authService.getCurrentUser());
        subreddit = subredditRepository.save(subreddit);
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }
}
