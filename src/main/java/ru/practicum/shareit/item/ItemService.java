package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto add(ItemDto itemDto, int owner);

    ItemDto getItemById(int id, @RequestHeader("X-Sharer-User-Id") int owner);

    Collection<ItemDto> getAllToOwner(int owner);

    ItemDto updateById(ItemDto itemDto, int id, int owner);

    Collection<ItemDto> search(String text);

    CommentDto addComment(CommentDto commentDto, int owner, int itemId);
}


