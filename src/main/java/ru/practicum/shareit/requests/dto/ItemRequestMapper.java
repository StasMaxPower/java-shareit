package ru.practicum.shareit.requests.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemMaper;
import ru.practicum.shareit.requests.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMaper.class})
public interface ItemRequestMapper {



    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    List<ItemRequestDto> toItemRequestDto(List<ItemRequest> itemRequest);




}
