package com.demisco.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItemEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn
    private OrderEntity order;

    @ManyToOne
    @JoinColumn
    private ProductEntity product;

    @Column
    private Integer quantity;

    @Column
    private Double totalPrice;

}