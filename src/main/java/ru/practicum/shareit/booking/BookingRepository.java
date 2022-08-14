package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("select b from Booking b, Item i where b.itemId=i.id and b.id=?1 and (b.booker.id=?2 or i.owner=?2 ) ")
    Optional<Booking> getBookingById(int bookingId, int userId);

    List<Booking> findBookingByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime localDateTime);

    List<Booking> findBookingByBookerOrderByStartDesc(User booker);

    List<Booking> findBookingByBookerAndStartAfterOrderByStartDesc(User booker, LocalDateTime localDateTime);

    List<Booking> findBookingByBookerAndStatusOrderByStartDesc(User booker, Status status);

    List<Booking> findBookingByBookerAndStartBeforeAndEndIsAfterOrderByStartDesc(User booker, LocalDateTime start, LocalDateTime end);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 order by b.start desc")
    List<Booking> findAllBookingByOwner(int owner);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 and b.end<?2 order by b.start desc")
    List<Booking> findBookingByOwnerPast(int owner, LocalDateTime localDateTime);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 and b.start>?2 order by b.start desc")
    List<Booking> findBookingByOwnerFuture(int owner, LocalDateTime localDateTime);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 and b.start<?2 " +
            "and b.end>?3 order by b.start desc")
    List<Booking> findBookingByOwnerCurrent(int owner, LocalDateTime localDateTimeStart,
                                            LocalDateTime localDateTimeEnd);

    @Query("select b from Booking b, Item i where b.itemId=i.id and i.owner=?1 and b.status=?2  order by b.start desc")
    List<Booking> findBookingByOwnerWaiting(int owner, Status status);

    boolean existsBookingByIdAndAndItemIdAndEndBefore(int id, int itemId, LocalDateTime localDateTime);


    @Query("select b from Booking b where b.itemId=?1 and b.start>?2 order by b.start desc ")
    List<Booking> getNextBookingToItem(int itemId, LocalDateTime localDateTime);

    @Query("select b from Booking b where b.itemId=?1 and b.end<?2 order by b.end desc ")
    List<Booking> getLastBookingToItem(int itemId, LocalDateTime localDateTime);

    Booking findFirstByItemIdAndEndBeforeOrderByEndDesc(int itemId, LocalDateTime localDateTime);

    Booking findFirstByItemIdAndStartAfterOrderByStartDesc(int itemId, LocalDateTime localDateTime);


}
