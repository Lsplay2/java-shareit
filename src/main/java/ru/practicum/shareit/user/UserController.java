package ru.practicum.shareit.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DuplicationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    public final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Поступил запрос на получение всех пользователей");
        return userService.getAll();
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable Long id) throws NotFoundException {
        log.info("Поступил запрос на получение пользователей по id:" + id);
        return userService.getById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException, DuplicationException {
        log.info("Поступил запрос на создание пользователя:" + user);
        userService.save(user);
        log.info("Пользователь добавлен в коллекцию:" + user);
        return user;
    }

    @PatchMapping (value = "/{id}")
    public String updateUser(@RequestBody User user, @PathVariable("id") Long userId)
            throws NotFoundException, ValidationException, DuplicationException {
        log.info("Поступил запрос на обновление пользователя:" + user);
        return userService.updateUser(user,userId);
    }

    @DeleteMapping(value = "/{id}")
    public void delUser(@PathVariable Long id) {
        log.info("Поступил запрос на удаление пользователя:" + id);
        userService.delUser(id);
        log.info("Пользователь удален. Текущее число пользователей:" + userService.getAll().size());
    }
}
