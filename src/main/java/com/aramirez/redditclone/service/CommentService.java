package com.aramirez.redditclone.service;

import com.aramirez.redditclone.dto.CommentsDto;
import com.aramirez.redditclone.exception.PostNotFoundException;
import com.aramirez.redditclone.mapper.CommentMapper;
import com.aramirez.redditclone.model.Comment;
import com.aramirez.redditclone.model.NotificationEmail;
import com.aramirez.redditclone.model.Post;
import com.aramirez.redditclone.model.User;
import com.aramirez.redditclone.repository.CommentRepository;
import com.aramirez.redditclone.repository.PostRepository;
import com.aramirez.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public CommentsDto save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentRepository.save(commentMapper
                .mapDtoToComment(commentsDto, post, authService.getCurrentUser()));

        String message = mailContentBuilder.build(authService.getCurrentUser().getUsername()
                + " posted a comment on your post " + post.getPostName());
        sendCommentNotification(message, post.getUser());

        return commentMapper.mapCommentToDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentsDto> getAllCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapCommentToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentsDto> getAllCommentsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapCommentToDto)
                .collect(Collectors.toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername()
                + " commented on your post", user.getEmail(), message));
    }
}
