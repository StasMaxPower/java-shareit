package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * // TODO .
 */
@Data
@NoArgsConstructor
public class Booking {

    private int id;
    private LocalDate start;
    private LocalDate end;
    private int item;
    private int booker;
    private String status;
}
