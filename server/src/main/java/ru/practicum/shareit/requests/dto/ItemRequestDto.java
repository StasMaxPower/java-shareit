package ru.practicum.shareit.requests.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
public class ItemRequestDto {
    private int id;
    @NotNull()
    private String description;
    private LocalDateTime created;
    private Set<ItemRequest.ShortItem> items;
}
