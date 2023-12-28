package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDao {
    private Map<Long, User> userMap = new HashMap<>();

    public User getById(Long id) {
        return userMap.get(id);
    }

    public void save(Long id, User user) {
        userMap.put(id, user);
    }

    public List<User> getValues() {
        return new ArrayList<>(userMap.values());
    }

    public void delete(Long id) {
        userMap.remove(id);
    }
}
