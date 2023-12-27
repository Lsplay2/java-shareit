package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findBookingById(Long userId);
    List<Booking> findByUserIdOrderByStartDesc(Long userId);
    List<Booking> findByUserIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime start);
    List<Booking> findByUserIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);
    List<Booking> findByUserIdAndStatusOrderByStartDesc(Long userId, String status);
    List<Booking> findByItemIdOrderByStartDesc(Long itemId);
    List<Booking> findByItemIdAndStatusOrderByStartDesc(Long itemId, String status);
    List<Booking> findByItemIdAndStartIsAfterOrderByStartDesc(Long itemId, LocalDateTime start);
    List<Booking> findByItemIdAndEndIsBeforeOrderByStartDesc(Long itemId, LocalDateTime end);
    List<Booking> findByItemIdOrderByEndDesc(Long itemId);
    Boolean existsByItemId(Long id);
    Boolean existsByItemIdAndUserId(Long itemId, Long userId);
    List<Booking> findByItemIdAndUserIdOrderByStartAsc(Long itemId, Long userId);
    Booking findFirstByOrderByIdDesc();
    @Query("SELECT b FROM Booking b WHERE :nowTime BETWEEN b.start AND b.end")
    List<Booking> findBookingWithDateBetweenNowByUserId(@Param("nowTime") LocalDateTime now);
    @Query("SELECT b FROM Booking b WHERE :nowTime BETWEEN b.start AND b.end")
    List<Booking> findBookingWithDateBetweenNowByItemId(@Param("nowTime") LocalDateTime now);

}
