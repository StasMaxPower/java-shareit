package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.ShortBooking;
import ru.practicum.shareit.item.Comment;

import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private int id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
    private int request;

    private List<CommentDto> comments;
    private ShortBooking lastBooking;
    private ShortBooking nextBooking;

/*@JsonIgnore
    private Booking lastBooking;
    @JsonIgnore
    private Booking nextBooking;*/
}
