package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.util.Date;

@Data
@Builder
public class ItemRequestDto {
    int id;
    String description;
    User requestor;
    Date created;
}
