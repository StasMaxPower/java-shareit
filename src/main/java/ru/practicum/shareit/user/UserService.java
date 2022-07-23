package ru.practicum.shareit.user;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto addUser(User user);

    Collection<UserDto> getAll();

    UserDto delete(int id);

    UserDto getToId(int id);

    UserDto updateToId(int id, User user);

    void checkUserToId(int id);
}
