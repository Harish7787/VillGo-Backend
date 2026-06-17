package com.VillGo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    private String image;

    private Boolean action = true;

    private Boolean deleted = false;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

//    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<Product> products;
}


