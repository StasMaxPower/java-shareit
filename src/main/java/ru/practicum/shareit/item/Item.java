package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private int requestId;


    @OneToMany(mappedBy = "itemId", fetch = FetchType.LAZY)
    //@JsonIgnore
    private List<Comment> comments;

    @Transient
    private ItemDto.ShortBookingDto lastBooking;
    @Transient
    private ItemDto.ShortBookingDto nextBooking;
}
