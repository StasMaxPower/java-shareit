package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {

    private int id;
    @NotBlank
    private final String text;
    private int item;
    private String authorName;
    private LocalDateTime created;
}
