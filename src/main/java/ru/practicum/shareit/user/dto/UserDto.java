package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class UserDto {

    private int id;
    private String name;
    @Email
    @NotNull
    private String email;
}
