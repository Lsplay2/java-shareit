package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.dto.CommentDto;
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
    public ItemDto addNewItem(@RequestHeader(value = "X-Sharer-User-Id") Long id, @RequestBody ItemDto itemDto)
            throws ValidationException, NotFoundException {
        return  itemService.save(itemDto, id);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") Long id,
                            @PathVariable("itemId") Long itemId,
                            @RequestBody ItemDto itemDto) throws ValidationException, NotFoundException {
        return itemService.changeItem(itemDto, id, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader(value = "X-Sharer-User-Id") Long userId) throws NotFoundException {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItem(@RequestHeader(value = "X-Sharer-User-Id") Long id) {
        return itemService.getAllItemByUser(id);
    }

    @GetMapping (value = "/search")
    public List<ItemDto> getItemBySearch(@RequestParam("text") String text) {
        return itemService.getBySearch(text);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody Comment comment) throws NotFoundException, ValidationException {
        return itemService.addComment(comment, itemId, userId);
    }

    @GetMapping (value = "/{itemId}/comment")
    public List<CommentDto> getAllComments(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                           @PathVariable Long itemId) {
        return itemService.getAllComments(itemId, userId);
    }

}
