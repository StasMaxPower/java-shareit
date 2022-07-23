package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemStorage itemStorage;
    private final UserService userService;
    private  int id;

    @Override
    public ItemDto add(Item item, int owner) {
        item.setOwner(owner);
        userService.checkUserToId(item.getOwner());
        log.info("Запрос на добавление новой вещи получен");
        item.setId(++id);
        return ItemMapper.toItemDto(itemStorage.add(item));
    }

    @Override
    public ItemDto getItemToId(int id) {
        log.info("Запрос на вывод вещи по ID получен");
        return ItemMapper.toItemDto(itemStorage.getToId(id));
    }

    @Override
    public Collection<ItemDto> getAllToOwner(int owner) {
        log.info("Запрос на вывод всех вещей получен");
        return itemStorage.getAllToOwner(owner).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto updateToId(Item newItem, int id, int owner) {
        log.info("Запрос на обновление вещи с ID = {} получен", id);
        Item item = itemStorage.getToId(id);
        if (item.getOwner()!= owner)
            throw new NotFoundException("Invalid user");
        if (newItem.getName() != null)
            item.setName(newItem.getName());
        if(newItem.getAvailable()!= null)
            item.setAvailable(newItem.getAvailable());
        if ((newItem.getDescription()!= null))
            item.setDescription(newItem.getDescription());
        return ItemMapper.toItemDto(itemStorage.add(item));
    }

    @Override
    public Collection<ItemDto> search(String text) {
        log.info("Запрос на поиск вещи с текстом {} получен", text);
        return itemStorage.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
