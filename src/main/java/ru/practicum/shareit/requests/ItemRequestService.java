package ru.practicum.shareit.requests;


import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto add(ItemRequestDto itemRequestDto, int userId);

    Collection<ItemRequestDto> getOwnRequests(int userId);

    ItemRequestDto getRequestById(int userId, int requestId);

    Collection<ItemRequestDto> getAllRequests(int from, int size, int userId);
}
