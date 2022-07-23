package ru.practicum.shareit.user;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserService {
    User addUser(User user);
    Collection<User> getAll();

    User delete(int id);

    User getToId(int id);

    User updateToId(int id, User user);

    void checkUserToId(int id);
}
