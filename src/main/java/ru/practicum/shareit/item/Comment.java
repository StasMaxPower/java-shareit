package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    @Column(name = "item_id", nullable = false)
    private int itemId;


    @Column(name = "author_id", nullable = false)
    private int authorId;

    @ManyToOne()
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private User author;

    @CreationTimestamp
    private LocalDateTime created;

}
