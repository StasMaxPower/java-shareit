package ru.practicum.shareit.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
@NoArgsConstructor
public class ItemRequest {

    private int id;
    private String desription;
    private int requestor;
    private LocalDateTime created;
}
