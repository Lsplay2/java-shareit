package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    public final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping()
    public ItemDto addNewItem(@RequestHeader(value = "X-Sharer-User-Id") Integer id, @RequestBody ItemDto itemDto)
            throws ValidationException, NotFoundException {
        return  itemService.save(itemDto, id);
    }

    @PatchMapping(value = "/{itemId}")
    public String updateItem(@RequestHeader(value = "X-Sharer-User-Id") Integer id,
                            @PathVariable("itemId") Integer itemId,
                            @RequestBody ItemDto itemDto) throws ValidationException, NotFoundException {
        return itemService.changeItem(itemDto,id, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItem(@PathVariable Integer itemId) throws NotFoundException {
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItem(@RequestHeader(value = "X-Sharer-User-Id") Integer id) {
        return itemService.getAllItemByUser(id);
    }

    @GetMapping (value = "/search")
    public List<ItemDto> getItemBySearch(@RequestParam("text") String text) {
        return itemService.getBySearch(text);
    }


}
