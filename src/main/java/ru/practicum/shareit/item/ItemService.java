package ru.practicum.shareit.item;

import java.util.Collection;

public interface ItemService {
    Item add(Item item, int owner);

    Item getItemToId(int id);

    Collection<Item> getAllToOwner(int owner);

    Item updateToId(Item item, int id, int owner);

    Collection<Item> search(String text);
}


