package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DublicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userStorage;

    @Override
    public UserDto addUser(UserDto userDto) {
        log.info("Запрос на добавление пользователей получен");
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.save(user));
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Запрос на вывод пользователей получен");
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto delete(int id) {
        log.info("Запрос на удаление пользователей получен");
        userStorage.deleteById(id);
        return null;
    }

    @Override
    public UserDto getById(int id) {
        log.info("Запрос на вывод пользователей по ID получен");
        return UserMapper.toUserDto(userStorage.findById(id).orElseThrow(() -> new NotFoundException("Не найден пользователь")));
    }

    @Override
    public UserDto updateById(int id, UserDto userDto) {
        log.info("Запрос на обновление пользователей по ID получен");
        User newUser = UserMapper.toUser(userDto);
        checkDuplicateEmail(newUser);
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Не найден пользователь"));
        if (newUser.getEmail() != null)
            user.setEmail(newUser.getEmail());
        if (newUser.getName() != null)
            user.setName(newUser.getName());
        return UserMapper.toUserDto(userStorage.save(user));
    }

    void checkDuplicateEmail(User user) {
        if (userStorage.findAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.info("Пользователь с таким Email уже существует");
            throw new DublicateEmailException("Пользователь с таким Email уже существует");
        }
    }

}
