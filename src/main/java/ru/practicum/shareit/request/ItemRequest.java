package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.util.Date;

@Data
public class ItemRequest {
    int id;
    String description;
    User requestor;
    Date created;
}
