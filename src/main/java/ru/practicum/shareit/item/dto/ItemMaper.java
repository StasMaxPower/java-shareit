package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingMaper;
import ru.practicum.shareit.item.Item;

@Mapper(componentModel = "spring", uses = {CommentMaper.class, BookingMaper.class})
public interface ItemMaper {

    Item toItem(ItemDto itemDto);

    @Mapping(source = "lastBooking", target = "lastBooking")
    @Mapping(source = "nextBooking", target = "nextBooking")
    ItemDto toDto(Item item);
}
