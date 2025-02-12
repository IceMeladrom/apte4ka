package ru.bmstu.my_apte4ka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double price;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "path_to_image", nullable = false, columnDefinition = "varchar(255) default 'default-item.png'")
    private String pathToImage = "default-item.png";


    @PrePersist
    public void prePersist() {
        if (this.pathToImage == null || this.pathToImage.isEmpty()) {
            this.pathToImage = "/img/default-item.png";
        }
    }
}
