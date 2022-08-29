package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMaper;
import ru.practicum.shareit.booking.dto.BookingMaperImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    ItemService itemService;
    @Mock
    ItemRepository itemRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;

    ItemMaper itemMaper = new ItemMaperImpl();
    CommentMaper commentMaper = new CommentMaperImpl();
    BookingMaper bookingMaper = new BookingMaperImpl();

    Item item = Item.builder().owner(3).name("test").description("testDesc").build();
    ItemDto itemDto = ItemDto.builder().name("testDto").description("testDescDto")
            .available(true).build();
    Comment comment = Comment.builder().text("test").build();

    @BeforeEach
    void before() {
        ReflectionTestUtils.setField(itemMaper, "commentMaper", commentMaper);
        itemService = new ItemServiceImpl(
                bookingMaper,
                itemMaper,
                commentMaper,
                itemRepository,
                userRepository,
                commentRepository,
                bookingRepository);
    }

    @Test
    void add_ShouldBeOk() {
        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(itemRepository.save(any())).thenReturn(item);
        ItemDto savedDto = itemService.add(itemDto, 1);
        assertEquals(item.getName(), savedDto.getName());
        Mockito.verify(itemRepository, Mockito.atMost(1)).save(any());
    }

    @Test
    void add_ShouldBeFail() {
        Exception ex = assertThrows(NotFoundException.class, () -> itemService.add(itemDto, anyInt()));
        assertEquals(ex.getMessage(), "Не найден текущий пользователь");
    }

    @Test
    void getItemById_ShouldBeOk() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        ItemDto savedDto = itemService.getItemById(anyInt(), 1);
        assertEquals(item.getName(), savedDto.getName());
        Mockito.verify(itemRepository, Mockito.times(1)).findById(anyInt());
    }

    @Test
    void getItemById_ShouldBeOkWith() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        ItemDto savedDto = itemService.getItemById(anyInt(), 3);
        assertEquals(item.getName(), savedDto.getName());
        Mockito.verify(itemRepository, Mockito.times(1)).findById(anyInt());
    }

    @Test
    void getItemById_ShouldBeFail() {
        NotFoundException ex = assertThrows(NotFoundException.class, () -> itemService.getItemById(anyInt(), 1));
        assertEquals(ex.getMessage(), "Не найдена вещь с ID");
    }

    @Test
    void getAllToOwner() {
        when(itemRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(List.of(item)));
        Collection<ItemDto> saved = itemService.getAllToOwner(0, 0, 1);
        assertEquals(saved.size(), 0);
    }

    @Test
    void getAllToOwnerWithoutPag() {
        when(itemRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(List.of(item)));
        Collection<ItemDto> saved = itemService.getAllToOwner(-100, -100, 1);
        assertEquals(saved.size(), 0);
    }

    @Test
    void updateById_ShouldBeOk() {
        when(itemRepository.save(any())).thenReturn(item);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        ItemDto savedDto = itemService.updateById(itemDto, 0, 3);
        assertEquals(item.getName(), savedDto.getName());
        Mockito.verify(itemRepository, Mockito.times(1)).save(any());
    }

    @Test
    void updateById_ShouldFail() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemService.updateById(itemDto, 1, 1));
        assertEquals(ex.getMessage(), "Invalid user");
    }

    @Test
    void search_ShouldBeOk() {
        when(itemRepository.search(any(), any())).thenReturn(new PageImpl<>(List.of(item)));
        Collection<ItemDto> saved = itemService.search("test", 0, 1);
        assertEquals(saved.size(), 1);
        assertTrue(saved.contains(itemMaper.toDto(item)));
    }

    @Test
    void search_ShouldBeOkWithoutPag() {
        when(itemRepository.search(any(), any())).thenReturn(new PageImpl<>(List.of(item)));
        Collection<ItemDto> saved = itemService.search("test", -100, -100);
        assertEquals(saved.size(), 1);
        assertTrue(saved.contains(itemMaper.toDto(item)));
    }

    @Test
    void search_ShouldBeOkEmptyList() {
        Collection<ItemDto> saved = itemService.search("", 0, 1);
        assertEquals(saved.size(), 0);
    }

    @Test
    void addComment_ShouldBeOk() {
        when(commentRepository.save(any())).thenReturn(comment);
        when(bookingRepository.existsBookingByIdAndAndItemIdAndEndBefore(anyInt(), anyInt(), any()))
                .thenReturn(true);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        CommentDto saved = itemService.addComment(CommentDto.builder().text("test").build(), 1, 1);
        assertEquals(saved.getText(), comment.getText());
    }

    @Test
    void addComment_ShouldBeFail() {
        ValidateException ex = assertThrows(ValidateException.class,
                () -> itemService.addComment(CommentDto.builder().text("test").build(), 1, 1));
        assertEquals(ex.getMessage(), "Такого букинга не существует или данный букинг еще не закончился");
    }

}