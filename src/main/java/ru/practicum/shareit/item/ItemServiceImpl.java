package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemStorage;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

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
    @Override
    public CommentDto addComment(CommentDto commentDto, int owner, int itemId){
        if (!bookingRepository.existsBookingByIdAndAndItemIdAndEndBefore(owner, itemId, LocalDateTime.now()))
            throw new ValidateException("Такого букинга не существует");
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItemId(itemId);
        comment.setAuthor(owner);
        User user = userRepository.findById(owner).orElseThrow(()->new NotFoundException("пользователя с таким ID нет"));
        comment = commentRepository.save(comment);
        return  CommentMapper.toCommentDto(comment, user.getName());
    }

/*    public Set<Comment> getCommentByItem(int itemId){
        return commentRepository.findCommentByItem(itemId);
    }*/
}
