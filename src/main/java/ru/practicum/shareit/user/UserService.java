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
    private UserRepository userRepository;
    private Gson gson = new Gson();

    @Autowired
    private UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void save(User user) throws ValidationException, DuplicationException {
        validateCreateUser(user);
        userRepository.save(user);
    }

    public String updateUser(User user, Long id) throws NotFoundException, ValidationException, DuplicationException {
        validateUpdateUser(id, user);
        User userBefore = userRepository.getUserById(id);
        if (user.getEmail() != null) {
            if (!user.getEmail().contains("@")) {
                throw new NotFoundException("Wrong email");
            }
            userBefore.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userBefore.setName(user.getName());
        }

        try {
            userRepository.save(userBefore);
            return gson.toJson(userBefore);
        } catch (Exception e) {
            throw new DuplicationException("Duplicate exception");
        }
    }

    public User getById(Long id) throws NotFoundException {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Not found user");
        }
        return userRepository.getUserById(id);
    }

    public List<User> getAll() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public void delUser(Long id) {
        userRepository.deleteById(id);
    }

    private void validateCreateUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")
                || user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Указаны не все данные");
        }
    }

    private void validateUpdateUser(Long id, User user) throws NotFoundException, ValidationException {
        if (userRepository.getUserById(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (user.getEmail() == null && Objects.equals(userRepository.getUserById(id).getEmail(), user.getEmail())) {
            throw new ValidationException("Wrong email");
        }
    }
}