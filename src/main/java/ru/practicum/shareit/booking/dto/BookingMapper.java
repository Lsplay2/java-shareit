package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder().booker(booking.getBooker())
                .id(booking.getId())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .item(booking.getItem())
                .build();
    }
}
