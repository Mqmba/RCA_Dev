package be.api.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Post")
public class Post extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PostId")
    private int id;

    @Column(name="Title", columnDefinition = "TEXT")
    private String title;

    @Column(name="Content", columnDefinition = "TEXT")
    private String content;

    @Column(name="Image", columnDefinition = "TEXT")
    private String image;


    @ManyToOne
    @JoinColumn(name = "UserId",  referencedColumnName = "UserId")
    private User user;

    @Column(name="Status")
    private int status;

}
