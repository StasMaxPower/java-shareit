package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoToIn {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
    private int booker;
    private Status status;
}
