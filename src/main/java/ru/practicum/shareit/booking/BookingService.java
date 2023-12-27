package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForCreate;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemService itemService;
    private ItemRepository itemRepository;
    private BookingMapper bookingMapper;


    @Autowired
    private BookingService(BookingRepository bookingRepository,
                            BookingMapper bookingMapper,
                            ItemService itemService,
                            UserRepository userRepository,
                            ItemRepository itemRepository) {
        this.bookingMapper = bookingMapper;
        this.bookingRepository = bookingRepository;
        this.itemService = itemService;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public BookingDtoForCreate save(BookingDto bookingDto) throws ValidationException, NotFoundException {
        validateCreateBooking(bookingDto.getBookerId(), bookingDto.getItemId(),
                bookingDto.getStart(), bookingDto.getEnd());
        if (!itemRepository.getById(bookingDto.getItemId()).getAvailable()) {
            throw new ValidationException("Unavailable item");
        }
        Booking booking = bookingMapper.toBooking(bookingDto);
        if (booking.getUser().getId() == booking.getItem().getUserId()) {
            throw new NotFoundException("It's your item");
        }
        booking.setStatus(Status.WAITING.toString());
        bookingRepository.save(booking);
        return bookingMapper.toBookingDtoForCreate(bookingRepository.findFirstByOrderByIdDesc());
    }

    public BookingDtoForCreate checkStatus(Long id, Boolean approve,Long userId) throws NotFoundException, ValidationException {
        Booking booking = bookingRepository.findBookingById(id);

        if (booking.getItem().getUserId() != userId) {
            throw new NotFoundException("Not found");
        }
        if (booking.getStatus().equals(Status.APPROVED.toString())) {
            throw new ValidationException("At now this is approved");
        }

        if (approve) {
           booking.setStatus(Status.APPROVED.toString());
        } else {
            booking.setStatus(Status.REJECTED.toString());
        }
        bookingRepository.save(booking);
        return bookingMapper.toBookingDtoForCreate(booking);
    }

    public BookingDtoForCreate getById(Long id, Long userId) throws NotFoundException {
        validateNotFoundBooking(id);
        validateNotFoundUser(userId);
        Booking bookingForFind = bookingRepository.findBookingById(id);
        if (bookingForFind.getUser().getId() == userId || bookingForFind.getItem().getUserId() == userId) {
            return bookingMapper.toBookingDtoForCreate(bookingRepository.findBookingById(id));
        }
        throw new NotFoundException("Not found");
    }

    public List<BookingDtoForCreate> getAll() {
        return bookingMapper.toDtoForCreateList(bookingRepository.findAll());
    }

    public List<BookingDtoForCreate> getAllByUser(Long userId, String state) throws NotFoundException, StatusException {
        validateNotFoundUser(userId);
        if (state == null || state.isBlank()) {
            state = "ALL";
        }
        validateWrongState(state);
        if (state.equals("ALL")) {
            return bookingMapper.toDtoForCreateList(bookingRepository.findByUserIdOrderByStartDesc(userId));
        } else {
            if (state.equals("FUTURE")) {
                return bookingMapper.toDtoForCreateList(bookingRepository.findByUserIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now()));
            }
            if (state.equals("PAST")) {
                return bookingMapper.toDtoForCreateList(bookingRepository.findByUserIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now()));
            }
            if (state.equals("CURRENT")) {
                List<Booking> bookingsTest = bookingRepository.findBookingWithDateBetweenNowByUserId(LocalDateTime.now());
                List<Booking> currentBookings = new ArrayList<>();
                for (Booking booking: bookingsTest) {
                    if (Objects.equals(booking.getUser().getId(),userId)) {
                        currentBookings.add(booking);
                    }
                }
                return bookingMapper.toDtoForCreateList(currentBookings);
            }
            return bookingMapper.toDtoForCreateList(bookingRepository.findByUserIdAndStatusOrderByStartDesc(userId, state));
        }

    }

    public List<BookingDtoForCreate> getAllBookingForUserItems(Long userId, String state) throws NotFoundException, StatusException {
        validateNotFoundUser(userId);
        if (state == null || state.isBlank()) {
            state = "ALL";
        }
        validateWrongState(state);
        List<ItemDto> items = itemService.getAllItemByUser(userId);
        List<Booking> bookings = new ArrayList<>();
        for (ItemDto itemDto : items) {
            if (state.equals("ALL")) {
                bookings.addAll(bookingRepository.findByItemIdOrderByStartDesc(itemDto.getId()));
            } else {
                if (state.equals("FUTURE")) {
                    bookings.addAll(bookingRepository.findByItemIdAndStartIsAfterOrderByStartDesc(itemDto.getId(), LocalDateTime.now()));
                }
                if (state.equals("PAST")) {
                    bookings.addAll(bookingRepository.findByItemIdAndEndIsBeforeOrderByStartDesc(itemDto.getId(), LocalDateTime.now()));
                }
                if (state.equals("CURRENT")) {
                    List<Booking> bookingsTest = bookingRepository.findBookingWithDateBetweenNowByItemId(LocalDateTime.now());
                    for (Booking booking: bookingsTest) {
                        if (Objects.equals(booking.getItem().getId(), itemDto.getId())) {
                            bookings.add(booking);
                        }
                    }
                }
                bookings.addAll(bookingRepository.findByItemIdAndStatusOrderByStartDesc(itemDto.getId(), state));
            }
        }
        return bookingMapper.toDtoForCreateList(bookings);
    }

    private void validateCreateBooking(Long userId, Long itemId, LocalDateTime start,
                                       LocalDateTime end) throws NotFoundException, ValidationException {
        validateNotFoundUser(userId);
        validateNotFoundItem(itemId);
        validateDate(start, end);
    }

    private void validateWrongState(String state) throws StatusException {
        if (state != null && !state.isBlank() && !state.equals("ALL") && !state.equals("APPROVED")
                && !state.equals("WAITING") && !state.equals("REJECTED") && !state.equals("CANCELED")
                && !state.equals("FUTURE") && !state.equals("PAST") && !state.equals("CURRENT"))  {
            throw new StatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private void validateNotFoundUser(Long userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Wrong userId");
        }
    }

    private void validateNotFoundItem(Long itemId) throws NotFoundException, ValidationException {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Wrong itemId");
        }
    }

    private void validateNotFoundBooking(Long bookingId) throws NotFoundException {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException("Wrong bookingId");
        }
    }

    private void validateDate(LocalDateTime start, LocalDateTime end) throws ValidationException {
        if (start == null || end == null || end.isBefore(start)
                || end.isBefore(LocalDateTime.now())
                || start.isBefore(LocalDateTime.now())
                || end.isEqual(start)) {
            throw new ValidationException("Wrong time");
        }
    }
}
