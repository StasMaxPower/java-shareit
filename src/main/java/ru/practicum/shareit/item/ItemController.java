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
    public ItemDto addItem(@RequestBody @Valid Item item, @RequestHeader("X-Sharer-User-Id") int owner) {
        return itemService.add(item, owner);
    }

    @GetMapping("/{id}")
    public ItemDto getItemToId(@PathVariable int id){
        return itemService.getItemToId(id);
    }

    @GetMapping
    Collection<ItemDto>  getAllItemsToOwner(@RequestHeader("X-Sharer-User-Id") int owner){
        return itemService.getAllToOwner(owner);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItemToId(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") int owner,
                               @RequestBody Item item){
        return itemService.updateToId(item, id, owner);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItem(@RequestParam String text){
        return  itemService.search(text);
    }

}
