package com.aramirez.redditclone.mapper;

import com.aramirez.redditclone.dto.PostRequest;
import com.aramirez.redditclone.dto.PostResponse;
import com.aramirez.redditclone.model.Post;
import com.aramirez.redditclone.model.Subreddit;
import com.aramirez.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    Post mapDtoToPost(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapPostToDto(Post post);
}
