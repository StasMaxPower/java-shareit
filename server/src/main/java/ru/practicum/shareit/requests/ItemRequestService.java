package ru.practicum.shareit.requests;


import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto add(ItemRequestDto itemRequestDto, int userId);

    List<ItemRequestDto> getOwnRequests(int userId);

    ItemRequestDto getRequestById(int userId, int requestId);

    List<ItemRequestDto> getAllRequests(int from, int size, int userId);
}
