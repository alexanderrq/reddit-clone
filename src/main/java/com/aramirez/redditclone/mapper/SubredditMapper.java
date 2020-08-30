package com.aramirez.redditclone.mapper;

import com.aramirez.redditclone.dto.SubredditDto;
import com.aramirez.redditclone.model.Post;
import com.aramirez.redditclone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "postCount", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);

    default Integer mapPosts(List<Post> posts) {
        return posts.size();
    }
}
