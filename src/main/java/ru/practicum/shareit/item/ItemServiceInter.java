package ru.practicum.shareit.item;


import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemServiceInter {
    ItemDto save(ItemDto itemDto, Long owner) throws ValidationException, NotFoundException;

    ItemDto changeItem(ItemDto itemDto, Long userId, Long itemId) throws NotFoundException;

    ItemDto getById(Long id, Long userId) throws NotFoundException;

    List<ItemDto> getAll();

    List<ItemDto> getAllItemByUser(Long id);

    List<ItemDto> getBySearch(String text);
}
