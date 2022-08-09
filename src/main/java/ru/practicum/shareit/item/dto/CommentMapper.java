package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CommentMapper {
    private final UserRepository userRepository;


    public static Comment toComment(CommentDto commentDto) {

        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItem(),
                0,
                LocalDateTime.now()
        );
    }

    public static CommentDto toCommentDto(Comment comment, String userName) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItemId(),
                userName,
                comment.getCreated()
        );
    }
}
