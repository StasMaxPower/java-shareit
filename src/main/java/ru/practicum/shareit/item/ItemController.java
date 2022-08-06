package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collection;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int owner) {
        return itemService.add(itemDto, owner);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable int id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    Collection<ItemDto> getAllItemsToOwner(@RequestHeader("X-Sharer-User-Id") int owner) {
        return itemService.getAllToOwner(owner);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItemById(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") int owner,
                                  @RequestBody ItemDto itemDto) {
        return itemService.updateById(itemDto, id, owner);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("{itemId}/comment")
    public ItemDto addComment(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int owner) {
        return null;
    }
}
