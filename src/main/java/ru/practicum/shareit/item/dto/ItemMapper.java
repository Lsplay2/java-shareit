package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemdto = ItemDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .description(item.getDescription())
                .name(item.getName())
                .build();
        return itemdto;
    }

    public static Item toItem(ItemDto itemDto) {
         Item item = new Item();
         item.setId(itemDto.getId());
         item.setAvailable(itemDto.getAvailable());
         item.setDescription(itemDto.getDescription());
         item.setName(itemDto.getName());
         return item;
    }

    public static List<ItemDto> toListDto(List<Item> items) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(toItemDto(item));
        }
        return itemDtos;
    }
}
