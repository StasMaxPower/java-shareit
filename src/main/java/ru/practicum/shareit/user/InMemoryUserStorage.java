package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage {
    private Map<Integer, User> users = new HashMap<>();

    public User add(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public User delete(int id) {
        User user = getToId(id);
        users.remove(id);
        return user;
    }

    public User getToId(int id) {
        return users.get(id);
    }

    public User update(int id, User newUser) {
        User user = getToId(id);
        if (newUser.getEmail() != null)
            user.setEmail(newUser.getEmail());
        if (newUser.getName() != null)
            user.setName(newUser.getName());
        users.put(user.getId(), user);
        return user;
    }
}
