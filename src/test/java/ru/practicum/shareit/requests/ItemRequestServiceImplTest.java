package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMaper;
import ru.practicum.shareit.item.dto.ItemMaperImpl;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestMapperImpl;
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
class ItemRequestServiceImplTest {

    @Mock
    ItemRequestsRepository itemRequestsRepository;
    ItemRequestMapper itemRequestMapper = new ItemRequestMapperImpl();
    @Mock
    private UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    ItemMaper itemMaper = new ItemMaperImpl();

    ItemRequestService itemRequestService;

    ItemRequestDto itemRequestDto = ItemRequestDto.builder().description("test").build();
    ItemRequest itemRequest = ItemRequest.builder().description("test").build();
    ItemRequest newItemRequest = ItemRequest.builder().description("newTest").build();
    User user = User.builder().name("user").email("test@email.ru").build();


    @BeforeEach
    void before() {
        itemRequestService = new ItemRequestServiceImpl(
                itemRequestsRepository,
                itemRequestMapper,
                userRepository,
                itemRepository,
                itemMaper);
    }

    @Test
    void add_ShouldBeOk() {
        when(itemRequestsRepository.save(any())).thenReturn(itemRequest);
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        ItemRequestDto saved = itemRequestService.add(itemRequestDto, 1);
        assertEquals(saved.getDescription(), itemRequest.getDescription());
    }

    @Test
    void add_ShouldBeFailed() {
        Exception ex = assertThrows(NotFoundException.class, () -> itemRequestService.add(itemRequestDto, 1));
        assertEquals(ex.getMessage(), "Пользователь не найден");
    }

    @Test
    void getOwnRequests_ShouldReturnListOfTwoElements() {
        when(itemRequestsRepository.findAllByRequestorOrderByCreatedDesc(anyInt()))
                .thenReturn(List.of(itemRequest, newItemRequest));
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        Collection<ItemRequestDto> list = itemRequestService.getOwnRequests(1);
        assertEquals(list.size(), 2);
        assertTrue(list.contains(itemRequestMapper.toItemRequestDto(itemRequest)));
        assertTrue(list.contains(itemRequestMapper.toItemRequestDto(newItemRequest)));
    }

    @Test
    void getOwnRequests_ShouldBeFailed() {
        Exception ex = assertThrows(NotFoundException.class, () -> itemRequestService.getOwnRequests(anyInt()));
        assertEquals(ex.getMessage(), "Пользователь не найден");
    }

    @Test
    void getAllRequests_ShouldBeOk() {
        when(itemRequestsRepository.findAll((Pageable) any()))
                .thenReturn((new PageImpl<>(List.of(itemRequest, newItemRequest))));
        Collection<ItemRequestDto> list = itemRequestService.getAllRequests(1, 1, 1);
        assertEquals(list.size(), 2);
        assertTrue(list.contains(itemRequestMapper.toItemRequestDto(itemRequest)));
        assertTrue(list.contains(itemRequestMapper.toItemRequestDto(newItemRequest)));
    }

    @Test
    void getRequestById_ShouldBeOk() {
        when(itemRequestsRepository.findById(anyInt())).thenReturn(Optional.ofNullable(itemRequest));
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        ItemRequestDto saved = itemRequestService.getRequestById(1, 1);
        assertEquals(saved.getDescription(), itemRequest.getDescription());
    }

    @Test
    void getRequestById_ShouldBeFailed() {
        Exception ex = assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(anyInt(), 1));
        assertEquals(ex.getMessage(), "Пользователь не найден");
    }

    @Test
    void getRequestById_ShouldBeFail() {
        when(itemRequestsRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user));
        Exception ex = assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(anyInt(), 1));
        assertEquals(ex.getMessage(), "Не найден запрос с ID");
    }
}