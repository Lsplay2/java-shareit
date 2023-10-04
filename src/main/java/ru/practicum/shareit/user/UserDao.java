package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDao {
    private Map<Integer, User> userMap = new HashMap<>();

    public User getById(int id) {
        return userMap.get(id);
    }

    public void save(int id, User user) {
        userMap.put(id, user);
    }

    public List<User> getValues() {
        return new ArrayList<>(userMap.values());
    }

    public void delete(int id) {
        userMap.remove(id);
    }
}
