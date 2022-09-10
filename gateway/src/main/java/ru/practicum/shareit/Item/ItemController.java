package ru.practicum.shareit.Item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Item.dto.CommentDto;
import ru.practicum.shareit.Item.dto.ItemDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long owner) {
        return itemClient.addItem(itemDto, owner);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") Long owner) {
        return itemClient.getItemById(id, owner);
    }

    @GetMapping
    ResponseEntity<Object> getAllItemsToOwner(@RequestHeader("X-Sharer-User-Id") int owner,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        return itemClient.getAllToOwner(owner, from, size);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItemById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long owner,
                                                 @RequestBody ItemDto itemDto) {
        return itemClient.updateById(itemDto, id, owner);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return itemClient.search(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Long owner,
                                             @PathVariable Long itemId) {
        return itemClient.addComment(commentDto, owner, itemId);
    }
}
