package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.Collection;

/**
 * // TODO .
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        return userService.getAll();
    }


    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable int id) {
        return userService.delete(id);
    }

    @GetMapping("/{id}")
    public UserDto getUserToID(@PathVariable int id) {
        return userService.getToId(id);
    }

    @PatchMapping("/{id}")
    public UserDto updateUserToId(@PathVariable int id, @RequestBody User user) {
        return userService.updateToId(id, user);
    }
}
