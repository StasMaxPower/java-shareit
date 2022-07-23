package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto add(Item item, int owner);

    ItemDto getItemToId(int id);

    Collection<ItemDto> getAllToOwner(int owner);

    ItemDto updateToId(Item item, int id, int owner);

    Collection<ItemDto> search(String text);
}


