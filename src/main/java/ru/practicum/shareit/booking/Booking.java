package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Date;

@Data
public class Booking {
    int id;
    Date start;
    Date end;
    Item item;
    User booker;
    Status status;
}

