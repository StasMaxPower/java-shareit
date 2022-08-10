package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortBooking {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int bookerId;
}
