package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Date;

@Data
@Builder
public class BookingDto {
    int id;
    Date start;
    Date end;
    Item item;
    User booker;
    Status status;
}
