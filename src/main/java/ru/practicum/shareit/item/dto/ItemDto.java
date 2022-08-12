package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.ShortBooking;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

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
