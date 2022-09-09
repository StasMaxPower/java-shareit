package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
public class ItemRequestDto {
    private int id;
    @NotNull(message = "Ошибка. Описание отсутствует")
    private String description;
    private LocalDateTime created;
    private Set<ShortItem> items;

    @Data
    @AllArgsConstructor
    public static class ShortItem {
        private int id;
        private String name;
        private String description;
        private Boolean available;
        private int requestId;
    }
}
