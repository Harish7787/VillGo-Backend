package com.VillGo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String image;

    private Boolean action = true;

    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

//    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<Product> products;
}


