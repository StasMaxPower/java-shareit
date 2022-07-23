package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Item;


public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != 0 ? item.getRequest() : 0
        );
    }

}
