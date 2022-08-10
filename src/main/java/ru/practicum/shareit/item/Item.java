package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.ShortBooking;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * // TODO .
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    @Column(name = "is_available", nullable = false)
    private Boolean available;
    @NotNull
    @Column(name = "owner_id", nullable = false)
    private int owner;
    @Column(name = "request_id", nullable = false)
    private int request;

    @OneToMany(mappedBy = "itemId", fetch = FetchType.LAZY)
    //@JsonIgnore
    private List<Comment> comments;

    @Transient
    private ShortBooking lastBooking;
    @Transient
    private ShortBooking nextBooking;
}
