package ru.practicum.shareit.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * // TODO .
 */
@Builder
@Data
public class ItemRequestDto {
    private int id;
    @NotNull(message = "Ошибка. Описание отсутствует")
    private String description;
    private LocalDateTime created;
    private Set<ItemRequest.ShortItem> items;
}
