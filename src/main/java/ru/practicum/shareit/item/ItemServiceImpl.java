package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMaper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final BookingMaper bookingMaper;
    private final ItemMaper itemMaper;
    private final CommentMaper commentMaper;
    private final ItemRepository itemStorage;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto add(ItemDto itemDto, int owner) {
        log.info("Запрос на добавление новой вещи получен");
        Item item = itemMaper.toItem(itemDto);
        item.setOwner(owner);
        userService.checkUserToId(item.getOwner());
        return itemMaper.toDto(itemStorage.save(item));
    }

    @Override
    public ItemDto getItemById(int id, @RequestHeader("X-Sharer-User-Id") int owner) {
        log.info("Запрос на вывод вещи по ID получен");
        Item item = itemStorage.findById(id).orElseThrow(() ->
                new NotFoundException("Не найдена вещь с ID " + id));
        if (item.getOwner() == owner) {
            item = addNextAndLastBookingToItem(item);
            return itemMaper.toDto(item);
        }
        return itemMaper.toDto(item);
    }

    @Override
    public Collection<ItemDto> getAllToOwner(int owner) {
        log.info("Запрос на вывод всех вещей получен");
        return
                itemStorage.findAll().stream()
                        .filter(item -> item.getOwner() == owner)
                        .map(item -> item = addNextAndLastBookingToItem(item))
                        .map(itemMaper::toDto)
                        .sorted(Comparator.comparingInt(ItemDto::getId))
                        .collect(Collectors.toList());
/*                itemStorage.getAllToOwner(owner).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());*/
    }

    @Override
    public ItemDto updateById(ItemDto newItem, int id, int owner) {
        log.info("Запрос на обновление вещи с ID = {} получен", id);
        Item item = itemStorage.findById(id).orElseThrow(() -> new NotFoundException("Не найдена вещь с ID " + id));
        if (item.getOwner() != owner)
            throw new NotFoundException("Invalid user");
        if (newItem.getName() != null)
            item.setName(newItem.getName());
        if (newItem.getAvailable() != null)
            item.setAvailable(newItem.getAvailable());
        if ((newItem.getDescription() != null))
            item.setDescription(newItem.getDescription());
        return itemMaper.toDto(itemStorage.save(item));
    }

    @Override
    public Collection<ItemDto> search(String text) {
        log.info("Запрос на поиск вещи с текстом {} получен", text);
        if (text.equals(""))
            return new ArrayList<>();
        return itemStorage.search(text).stream()
                .map(itemMaper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, int owner, int itemId) {
        log.info("Запрос на добавление комментария получен");
        if (!bookingRepository.existsBookingByIdAndAndItemIdAndEndBefore(owner, itemId, LocalDateTime.now()))
            throw new ValidateException("Такого букинга не существует или данный букинг еще не закончился");
        Comment comment = commentMaper.toComment(commentDto, owner);
        comment.setItemId(itemId);
        comment.setAuthorId(owner);
        comment.setAuthor(userRepository.findById(owner).orElseThrow());
        comment = commentRepository.save(comment);
        return commentMaper.toDto(comment);
    }

    public Item addNextAndLastBookingToItem(Item item) {
        Booking nextBooking = null;
        Booking lastBooking = null;
        List<Booking> bookingList = bookingRepository.getLastBookingToItem(item.getId(), LocalDateTime.now());
        if (bookingList != null && (!bookingList.isEmpty()))
            lastBooking = bookingList.get(0) == null ? null : bookingList.get(0);
        item.setLastBooking(bookingMaper.toShortBooking(lastBooking));
        bookingList = bookingRepository.getNextBookingToItem(item.getId(), LocalDateTime.now());
        if (bookingList != null && !bookingList.isEmpty())
            nextBooking = bookingList.get(0) == null ? null : bookingList.get(0);
        item.setNextBooking(bookingMaper.toShortBooking(nextBooking));
        return item;
    }

}
