package ru.bmstu.apte4ka.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String imageName;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long quantity;

}
