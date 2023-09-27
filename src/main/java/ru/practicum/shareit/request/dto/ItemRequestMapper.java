package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto (ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .id(itemRequest.getId())
                .requestor(itemRequest.getRequestor())
                .build();
    }
}
