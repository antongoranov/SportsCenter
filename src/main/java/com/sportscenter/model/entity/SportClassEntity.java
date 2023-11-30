package com.sportscenter.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sport_classes")
public class SportClassEntity extends BaseEntity {

    @ManyToOne(optional = false)
    private SportEntity sport;

    //@OneToOne(optional = false) //(optional = false, cascade = CascadeType.ALL)
    @ManyToOne(optional = false)
    private InstructorEntity instructor;

    @Column(name = "max_capacity")
    private int maxCapacity;

    @Column(name = "current_capacity")
    private int currentCapacity;

    //schedule (day of the week, time)
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @OneToMany(mappedBy = "sportClass", targetEntity = BookingEntity.class)
    private List<BookingEntity> bookings;
}
