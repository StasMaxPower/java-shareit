package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
public class UserDto {

    private int id;
    private String name;
    @Email
    @NotNull(message = "произошла ошибка. Имя пользователя отсутствует")
    private String email;
}
