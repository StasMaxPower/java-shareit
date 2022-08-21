package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
    private int requestId;

    private List<CommentDto> comments;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;


    @Data
    public static class ShortBookingDto {
        private int id;
        private LocalDateTime start;
        private LocalDateTime end;
        private int bookerId;
    }



/*@JsonIgnore
    private Booking lastBooking;
    @JsonIgnore
    private Booking nextBooking;*/
}
