package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto add(ItemDto itemDto, int owner);

    ItemDto getItemById(int id);

    Collection<ItemDto> getAllToOwner(int owner);

    ItemDto updateById(ItemDto itemDto, int id, int owner);

    Collection<ItemDto> search(String text);
}


