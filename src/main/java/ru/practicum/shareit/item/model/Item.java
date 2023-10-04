package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
@Builder
public class Item {
    int id;
    String name;
    String description;
    Boolean available;
    int owner;
    ItemRequest request;
}
