package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForCreate;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    public final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public BookingDtoForCreate addNewBooking(@RequestBody BookingDto booking, @RequestHeader(value = "X-Sharer-User-Id") Long id
                                    ) throws ValidationException, NotFoundException {
        booking.setBookerId(id);
        return  bookingService.save(booking);
    }

    @PatchMapping(value = "/{itemId}")
    public BookingDtoForCreate approveBooking(
            @RequestHeader(value = "X-Sharer-User-Id") Long id,
            @PathVariable("itemId") Long itemId,
            @RequestParam("approved") Boolean approve)
            throws ValidationException, NotFoundException {
        return  bookingService.checkStatus(itemId,approve, id);
    }

    @GetMapping(value = "/{itemId}")
    public BookingDtoForCreate getBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                           @PathVariable("itemId") Long itemId) throws NotFoundException {
        return  bookingService.getById(itemId, userId);
    }

    @GetMapping()
    public List<BookingDtoForCreate> getAllUserBooking(@RequestHeader(value = "X-Sharer-User-Id") Long id,
                                              @RequestParam(value = "state", required = false) String state) throws NotFoundException, StatusException {
        return  bookingService.getAllByUser(id, state);
    }

    @GetMapping(value = "/owner")
    public List<BookingDtoForCreate> getBookingToItemOfUser(@RequestHeader(value = "X-Sharer-User-Id") Long id,
                                              @RequestParam(value = "state", required = false) String state) throws NotFoundException, StatusException {
        return  bookingService.getAllBookingForUserItems(id, state);
    }
}
