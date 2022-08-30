package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DublicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@Transactional(propagation = Propagation.REQUIRED)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final UserService userService;
    @MockBean
    private final UserRepository userRepository;


    UserDto userDto = UserDto.builder().name("user").email("test@email.ru").build();
    User user = User.builder().name("user").email("test@email.ru").build();
    User newUser = User.builder().name("newUser").email("testNewUser@email.ru").build();
    UserDto nullUserDto = UserDto.builder().name(null).email(null).build();

    @Test
    void addUser_ShouldBeOk() {
        when(userRepository.save(any()))
                .thenReturn(user);
        UserDto savedUser = userService.addUser(userDto);
        assertEquals(savedUser.getEmail(), user.getEmail());
        assertEquals(savedUser.getName(), user.getName());
    }

    @Test
    void getAll_ShouldBeOk() {
        when(userRepository.findAll())
                .thenReturn(List.of(user, newUser));
        Collection<UserDto> users = userService.getAll();
        Assertions.assertNotNull(users);
        assertEquals(users.size(), 2);
    }

    @Test
    void delete_ShouldBeOk() {
        userService.delete(1);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    void getById_ShouldBeOk() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(user));
        UserDto savedUser = userService.getById(1);
        Assertions.assertNotNull(savedUser);
        assertEquals(savedUser.getName(), user.getName());
        assertEquals(savedUser.getEmail(), user.getEmail());
    }

    @Test
    void getById_ShouldBeFail() {
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Exception ex = Assertions.assertThrows(NotFoundException.class,
                () -> userService.getById(1));
        assertEquals(ex.getMessage(), "Не найден пользователь");
    }

    @Test
    void updateById_ShouldBeOk() {
        when(userRepository.save(any()))
                .thenReturn(user);
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(newUser));
        UserDto savedUser = userService.updateById(1, userDto);
        Assertions.assertNotNull(savedUser);
        assertEquals(savedUser.getName(), user.getName());
        assertEquals(savedUser.getEmail(), user.getEmail());
    }

    @Test
    void updateById_ShouldBeFail() {
        when(userRepository.save(any()))
                .thenReturn(user);
        Exception ex = Assertions.assertThrows(NotFoundException.class,
                () -> userService.updateById(1, userDto));
        assertEquals(ex.getMessage(), "Не найден пользователь");
    }

    @Test
    void updateById_ShouldBeFailWithNullName() {
        when(userRepository.save(any()))
                .thenReturn(user);
        when(userRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(newUser));
        UserDto savedUser = userService.updateById(1, nullUserDto);
        Assertions.assertNotNull(savedUser);
    }

    @Test
    void updateById_ShouldBeDuplicateEmailException() {
        when(userRepository.findAll())
                .thenReturn(List.of(user));
        Exception ex = Assertions.assertThrows(DublicateEmailException.class,
                () -> userService.updateById(1, userDto));
        assertEquals(ex.getMessage(), "Пользователь с таким Email уже существует");
    }

}