package com.demisco.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @OneToMany(fetch = FetchType.EAGER)
    private List<OrderItemEntity> items;

    @Column
    private Double totalPrice;

    @Column
    private Status status;

    @ManyToOne
    @JoinColumn
    private UserEntity user;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancelledAt;

    public enum Status {
        CANCELLED,
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }

}