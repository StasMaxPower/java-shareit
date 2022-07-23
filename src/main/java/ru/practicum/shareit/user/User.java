package ru.practicum.shareit.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
@NoArgsConstructor
public class User {

    private int id;
    private String name;
    @Email
    @NotNull
    private String email;
}
