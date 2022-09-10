package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select b from Booking b, Item i where b.itemId=i.id and b.id=?1 and (b.booker.id=?2 or i.owner=?2 ) ")
    Optional<Booking> getBookingById(int bookingId, int userId);

    Page<Booking> findBookingByBookerAndEndIsBeforeOrderByStartDesc(User booker,
                                                                    LocalDateTime localDateTime, Pageable p);

    Page<Booking> findBookingByBookerOrderByStartDesc(User booker, Pageable pageable);

    Page<Booking> findBookingByBookerAndStartAfterOrderByStartDesc(
            User booker, LocalDateTime localDateTime, Pageable pageable);

    Page<Booking> findBookingByBookerAndStatusOrderByStartDesc(User booker, Status status, Pageable pageable);

    Page<Booking> findBookingByBookerAndStartBeforeAndEndIsAfterOrderByStartDesc(
            User booker, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 order by b.start desc")
    Page<Booking> findAllBookingByOwner(int owner, Pageable pageable);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 and b.end<?2 order by b.start desc")
    Page<Booking> findBookingByOwnerPast(int owner, LocalDateTime localDateTime, Pageable pageable);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 and b.start>?2 order by b.start desc")
    Page<Booking> findBookingByOwnerFuture(int owner, LocalDateTime localDateTime, Pageable pageable);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 and b.start<?2 " +
            "and b.end>?3 order by b.start desc")
    Page<Booking> findBookingByOwnerCurrent(int owner, LocalDateTime localDateTimeStart,
                                            LocalDateTime localDateTimeEnd, Pageable pageable);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 and b.status=?2  order by b.start desc")
    Page<Booking> findBookingByOwnerWaiting(int owner, Status status, Pageable pageable);

    boolean existsBookingByItemAndEndBefore(Item item, LocalDateTime localDateTime);

    @Query("select b from Booking b where b.itemId=?1 and b.start>?2 order by b.start desc ")
    List<Booking> getNextBookingToItem(int itemId, LocalDateTime localDateTime);

    @Query("select b from Booking b where b.itemId=?1 and b.end<?2 order by b.end desc ")
    List<Booking> getLastBookingToItem(int itemId, LocalDateTime localDateTime);
}
