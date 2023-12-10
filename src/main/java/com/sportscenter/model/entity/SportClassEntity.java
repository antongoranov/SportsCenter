package com.sportscenter.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    //@OneToMany(mappedBy = "sportClass", targetEntity = BookingEntity.class)
    //private List<BookingEntity> bookings;

    public String getDayOfWeekDisplayName(){
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    public String getSportClassInfo(){
        return String.format("%s with %s - %s to %s",
                this.sport.getName(),
                this.instructor.getFullName(),
                this.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                this.endTime.format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
