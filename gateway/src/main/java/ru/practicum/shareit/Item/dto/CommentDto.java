package ru.practicum.shareit.Item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CommentDto {

    private int id;
    @NotBlank
    private final String text;
    private int item;
    private String authorName;
    private LocalDateTime created;
}
