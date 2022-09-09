package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, int owner);

    ItemDto getItemById(int id, @RequestHeader("X-Sharer-User-Id") int owner);

    List<ItemDto> getAllToOwner(int owner, int from, int size);

    ItemDto updateById(ItemDto itemDto, int id, int owner);

    List<ItemDto> search(String text, int from, int size);

    CommentDto addComment(CommentDto commentDto, int owner, int itemId);
}


