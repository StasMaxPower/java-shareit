package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemStorage;
    private final UserService userService;

    @Override
    public ItemDto add(ItemDto itemDto, int owner) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        userService.checkUserToId(item.getOwner());
        log.info("Запрос на добавление новой вещи получен");
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    @Override
    public ItemDto getItemById(int id) {
        log.info("Запрос на вывод вещи по ID получен");
        return ItemMapper.toItemDto(itemStorage.findById(id).orElseThrow(()->
                new NotFoundException("Не найдена вещь с ID "+id)));
    }

    @Override
    public Collection<ItemDto> getAllToOwner(int owner) {
        log.info("Запрос на вывод всех вещей получен");
        return
                itemStorage.findAll().stream().filter(item -> item.getOwner() == owner)
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
/*                itemStorage.getAllToOwner(owner).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());*/
    }

    @Override
    public ItemDto updateById(ItemDto newItem, int id, int owner) {
        log.info("Запрос на обновление вещи с ID = {} получен", id);
        Item item = itemStorage.findById(id).orElseThrow(()->new NotFoundException("Не найдена вещь с ID "+id));
        if (item.getOwner() != owner)
            throw new NotFoundException("Invalid user");
        if (newItem.getName() != null)
            item.setName(newItem.getName());
        if (newItem.getAvailable() != null)
            item.setAvailable(newItem.getAvailable());
        if ((newItem.getDescription() != null))
            item.setDescription(newItem.getDescription());
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    @Override
    public Collection<ItemDto> search(String text) {
        log.info("Запрос на поиск вещи с текстом {} получен", text);
        if (text.equals(""))
            return new ArrayList<>();
        return itemStorage.search(text).stream()
                  .map(ItemMapper::toItemDto)
                  .collect(Collectors.toList());
//                itemStorage.search(text).stream()
//                .map(ItemMapper::toItemDto)
//                .collect(Collectors.toList());
    }
}
