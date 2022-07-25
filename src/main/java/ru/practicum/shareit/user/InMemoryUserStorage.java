package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int id;

    public User add(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public User delete(int id) {
        User user = getById(id);
        users.remove(id);
        return user;
    }

    public User getById(int id) {
        return users.get(id);
    }

    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }
}
