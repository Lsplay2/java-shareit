package ru.practicum.shareit.user;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.*;

@Service
public class UserService {
    private UserDao userDao;
    private Gson gson = new Gson();
    private int id = 0;

    @Autowired
    private UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    private int getId() {
        return ++id;
    }


    public void save(User user) throws ValidationException, DuplicationException {
        validateCreateUser(user);
        user.setId(getId());
        userDao.save(user.getId(), user);
    }

    public String updateUser(User user, Integer id) throws NotFoundException, DuplicationException {
        validateUpdateUser(id, user);
        User userBefore = userDao.getById(id);
        if (user.getEmail() != null) {
            userBefore.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userBefore.setName(user.getName());
        }
        userDao.save(id, userBefore);
        System.out.println(userBefore);
        return gson.toJson(userBefore);
    }

    public User getById(Integer id) {
        return userDao.getById(id);
    }

    public List<User> getAll() {
        return userDao.getValues();
    }

    public void delUser(Integer id) {
        userDao.delete(id);
    }

    private void validateCreateUser(User user) throws ValidationException, DuplicationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")
           || user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Указаны не все данные");
        }
        checkDuplicateEmail(user.getEmail());
    }

    private void validateUpdateUser(Integer id, User user) throws NotFoundException, DuplicationException {
        if (userDao.getById(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (user.getEmail() != null && !Objects.equals(userDao.getById(id).getEmail(), user.getEmail())) {
            checkDuplicateEmail(user.getEmail());
        }
    }


    private void checkDuplicateEmail(String email) throws DuplicationException {
        for (User users : userDao.getValues()) {
            if (users.getEmail().equals(email)) {
                throw new DuplicationException("Email уже существует");
            }
        }
    }
}
