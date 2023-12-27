package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {

     UserRepository userRepository;
     ItemRepository itemRepository;

    @Autowired
    private BookingMapper (ItemRepository itemRepository, UserRepository userRepository){
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }


    public  BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder().bookerId(booking.getUser().getId())
                .id(booking.getId())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .itemId(booking.getItem().getId())
                .build();
    }

    public  Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(booking.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(itemRepository.getById(bookingDto.getItemId()));
        booking.setUser(userRepository.getUserById(bookingDto.getBookerId()));
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public  BookingDtoForCreate toBookingDtoForCreate(Booking booking) {
        return BookingDtoForCreate.builder().booker(UserMapper.toUserDto(booking.getUser()))
                .id(booking.getId())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .item(ItemMapper.toItemDto(booking.getItem()))
                .build();
    }

    public List<BookingDto> toDtoList(List<Booking> bookingList) {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookingList) {
            bookingDtoList.add(toBookingDto(booking));
        }
        return bookingDtoList;
    }

    public List<BookingDtoForCreate> toDtoForCreateList(List<Booking> bookingList) {
        List<BookingDtoForCreate> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookingList) {
            bookingDtoList.add(toBookingDtoForCreate(booking));
        }
        return bookingDtoList;
    }
}
