package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForCreate;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

public interface BookingServiceInter {
    public BookingDtoForCreate save(BookingDto bookingDto) throws ValidationException, NotFoundException;

    public BookingDtoForCreate checkStatus(Long id, Boolean approve,Long userId) throws NotFoundException, ValidationException;

    public BookingDtoForCreate getById(Long id, Long userId) throws NotFoundException;

    public List<BookingDtoForCreate> getAll();

    public List<BookingDtoForCreate> getAllByUser(Long userId, String state) throws NotFoundException, StatusException;

    public List<BookingDtoForCreate> getAllBookingForUserItems(Long userId, String state) throws NotFoundException, StatusException;
}
