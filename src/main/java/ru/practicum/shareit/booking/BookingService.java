package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    Map<Integer, Booking> bookingMap = new HashMap<>();

    public void save (Booking booking) {
        bookingMap.put(booking.getId(), booking);
    }

    public Booking getById (Integer id) {
        return bookingMap.get(id);
    }

    public List<Booking> getAll () {
        return new ArrayList<>(bookingMap.values());
    }


}
