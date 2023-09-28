package ru.practicum.shareit.item;


import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemServiceInter {
    ItemDto save(ItemDto itemDto, Integer owner) throws ValidationException, NotFoundException;

    String changeItem(ItemDto itemDto, Integer userId, Integer itemId) throws NotFoundException;

    ItemDto getById(Integer id) throws NotFoundException;

    List<ItemDto> getAll();

    List<ItemDto> getAllItemByUser(Integer id);

    List<ItemDto> getBySearch(String text);
}
