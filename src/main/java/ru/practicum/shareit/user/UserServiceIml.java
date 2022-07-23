package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DublicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceIml implements UserService {
    private final InMemoryUserStorage userStorage;
    private int id;

    @Override
    public UserDto addUser(User user) {
        log.info("Запрос на добавление пользователей получен");
        checkUserEmail(user);
        user.setId(++id);
        return UserMapper.toUserDto(userStorage.add(user));
    }

    @Override
    public Collection<UserDto> getAll() {
        log.info("Запрос на вывод пользователей получен");
        return userStorage.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto delete(int id) {
        log.info("Запрос на удаление пользователей получен");
        return UserMapper.toUserDto(userStorage.delete(id));
    }

    @Override
    public UserDto getToId(int id) {
        log.info("Запрос на вывод пользователей по ID получен");
        return UserMapper.toUserDto(userStorage.getToId(id));
    }

    @Override
    public UserDto updateToId(int id, User user) {
        log.info("Запрос на обновление пользователей по ID получен");
        checkDuplicateEmail(user);
        return UserMapper.toUserDto(userStorage.update(id, user));
    }


    void checkUserEmail(User user) {
        checkDuplicateEmail(user);
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            log.info("Неверно указана электронная почта");
            throw new ValidateException("Неверно указана электронная почта");
        }
    }

    void checkDuplicateEmail(User user) {
        if (userStorage.getAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.info("Пользователь с таким Email уже существует");
            throw new DublicateEmailException("Пользователь с таким Email уже существует");
        }
    }

    public void checkUserToId(int id) {
        if (getAll().stream().noneMatch(user -> user.getId() == id))
            throw new NotFoundException("Пользователь с таким ID не найден");
    }
}
