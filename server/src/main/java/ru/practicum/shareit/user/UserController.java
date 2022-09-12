package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }


    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable int id) {
        return userService.delete(id);
    }

    @GetMapping("/{id}")
    public UserDto getUserByID(@PathVariable int id) {
        return userService.getById(id);
    }

    @PatchMapping("/{id}")
    public UserDto updateUserById(@PathVariable int id, @RequestBody UserDto userDto) {
        return userService.updateById(id, userDto);
    }
}
