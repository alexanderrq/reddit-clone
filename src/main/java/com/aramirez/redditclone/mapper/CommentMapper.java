package com.aramirez.redditclone.mapper;

import com.aramirez.redditclone.dto.CommentsDto;
import com.aramirez.redditclone.model.Comment;
import com.aramirez.redditclone.model.Post;
import com.aramirez.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment mapDtoToComment(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", source = "post.postId")
    @Mapping(target = "userName", source = "user.username")
    CommentsDto mapCommentToDto(Comment comment);
}
