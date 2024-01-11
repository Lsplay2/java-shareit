package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@Builder
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    List<CommentDto> comments;
    BookingDto lastBooking;
    BookingDto nextBooking;
}
