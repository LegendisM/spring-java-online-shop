package com.demisco.quiz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Double price;

    @Column
    private Integer usableBalance;

    @Column
    private Integer lockedBalance;

    @Version
    @Column
    private Integer version;

}