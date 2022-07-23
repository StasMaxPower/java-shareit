package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Item addItem(@RequestBody @Valid Item item, @RequestHeader("X-Sharer-User-Id") int owner) {
        return itemService.add(item, owner);
    }

    @GetMapping("/{id}")
    public Item getItemToId(@PathVariable int id){
        return itemService.getItemToId(id);
    }

    @GetMapping
    Collection<Item>  getAllItemsToOwner(@RequestHeader("X-Sharer-User-Id") int owner){
        return itemService.getAllToOwner(owner);
    }

    @PatchMapping("/{id}")
    public Item updateItemToId(@PathVariable int id, @RequestHeader("X-Sharer-User-Id") int owner,
                               @RequestBody Item item){
        return itemService.updateToId(item, id, owner);
    }

    @GetMapping("/search")
    public Collection<Item> searchItem(@RequestParam String text){
        return  itemService.search(text);
    }

}
