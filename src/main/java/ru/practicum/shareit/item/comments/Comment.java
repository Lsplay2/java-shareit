package ru.practicum.shareit.item.comments;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @Column
     String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
     User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
     Item item;

    @Column
    LocalDateTime created;
}
