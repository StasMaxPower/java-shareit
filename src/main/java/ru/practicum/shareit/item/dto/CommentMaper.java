package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserService.class})
public interface CommentMaper {

    @Mapping(source = "userId", target = "authorId")
    Comment toComment(CommentDto commentDto, int userId);

    @Mapping(source = "userName", target = "authorName")
    CommentDto toDto(Comment comment, String userName);

    @Mapping(source = "author.name", target = "authorName")
    CommentDto toDto(Comment comment);

    List<CommentDto> commentListToCommentDtoList(List<Comment> sourceList);


}
