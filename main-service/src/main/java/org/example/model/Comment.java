package org.example.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToOne()
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @Column(nullable = false)
    private LocalDateTime created;
}
