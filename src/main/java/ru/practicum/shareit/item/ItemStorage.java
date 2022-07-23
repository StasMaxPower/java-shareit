package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();

    public Item add(Item item){
        items.put(item.getId(), item);
        return  item;
    }

    public Item getToId(int id){
        return items.get(id);
    }

    public Collection<Item> getAllToOwner(int owner){
        return items.values().stream()
                .filter(item -> item.getOwner() == owner)
                .collect(Collectors.toList());
    }

    public Collection<Item> search(String text){
        if (text.equals(""))
                return new ArrayList<>();
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

}
