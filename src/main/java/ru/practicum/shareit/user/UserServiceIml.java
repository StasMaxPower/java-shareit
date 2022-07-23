package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DublicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.User;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceIml implements UserService{
    private final InMemoryUserStorage userStorage;
    private int id;
    @Override
    public User addUser(User user) {
        log.info("Запрос на добавление пользователей получен");
        checkUserEmail(user);
        user.setId(++id);
        return userStorage.add(user);
    }

    @Override
    public Collection<User> getAll() {
        log.info("Запрос на вывод пользователей получен");
        return userStorage.getAll();
    }

    @Override
    public User delete(int id) {
        log.info("Запрос на удаление пользователей получен");
        return userStorage.delete(id);
    }

    @Override
    public User getToId(int id) {
        log.info("Запрос на вывод пользователей по ID получен");
        return userStorage.getToId(id);
    }

    @Override
    public User updateToId(int id, User user) {
        log.info("Запрос на обновление пользователей по ID получен");
        checkDuplicateEmail(user);
        return userStorage.update(id, user);
    }


    void checkUserEmail(User user){
        checkDuplicateEmail(user);
        if (user.getEmail() == null || user.getEmail().isBlank()||(!user.getEmail().contains("@"))) {
            log.info("Неверно указана электронная почта");
            throw new ValidateException("Неверно указана электронная почта");
        }
    }

    void checkDuplicateEmail(User user){
        if (userStorage.getAll().stream().anyMatch(u->u.getEmail().equals(user.getEmail()))) {
            log.info("Пользователь с таким Email уже существует");
            throw new DublicateEmailException("Пользователь с таким Email уже существует");
        }
    }

    public void checkUserToId(int id){
        if (getAll().stream().noneMatch(user->user.getId() == id))
            throw new NotFoundException("Пользователь с таким ID не найден");
    }
}
