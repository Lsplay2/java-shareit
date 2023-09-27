package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto (Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .description(item.getDescription())
                .name(item.getName())
                .build();
    }

    public static Item toItem (ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .available(itemDto.getAvailable())
                .description(itemDto.getDescription())
                .name(itemDto.getName())
                .build();
    }

    public static List<ItemDto> toListDto (List<Item> items) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for(Item item : items) {
            itemDtos.add(toItemDto(item));
        }
        return itemDtos;
    }
}
