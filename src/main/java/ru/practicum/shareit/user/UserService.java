package ru.practicum.shareit.user;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.*;

@Service
public class UserService {
    public Map<Integer, User> userMap = new HashMap<>();

    Gson gson = new Gson();
    private int id = 0;
    private int getId() {
        return ++id;
    }


    public void save (User user) throws ValidationException, DuplicationException {
        validateCreateUser(user);
        user.setId(getId());
        userMap.put(user.getId(), user);
    }

    public String updateUser (User user, Integer id) throws NotFoundException, DuplicationException {
        validateUpdateUser(id, user);
        User userBefore = userMap.get(id);
        if (user.getEmail() != null) {
            userBefore.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userBefore.setName(user.getName());
        }
        userMap.put(id, userBefore);
        System.out.println(userBefore);
        return gson.toJson(userBefore);
    }

    public User getById (Integer id) {
        return userMap.get(id);
    }

    public List<User> getAll () {
        return new ArrayList<>(userMap.values());
    }

    public void delUser (Integer id) {
        userMap.remove(id);
    }

    private void validateCreateUser(User user) throws ValidationException, DuplicationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")
           || user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Указаны не все данные");
        }
        checkDuplicateEmail(user.getEmail());
    }

    private void validateUpdateUser(Integer id, User user) throws NotFoundException, DuplicationException {
        if (userMap.get(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (user.getEmail() != null && !Objects.equals(userMap.get(id).getEmail(), user.getEmail())) {
            checkDuplicateEmail(user.getEmail());
        }
    }


    private void checkDuplicateEmail(String email) throws DuplicationException {
        for (User users : userMap.values()) {
            if (users.getEmail().equals(email)) {
                throw new DuplicationException("Email уже существует");
            }
        }
    }
}
