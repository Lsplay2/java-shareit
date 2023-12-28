package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.comments.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .id(comment.getId())
                .build();
        return commentDto;
    }

    public static List<CommentDto> toListCommentDto(List<Comment> commentList) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(toCommentDto(comment));
        }
        return commentDtoList;
    }
}
