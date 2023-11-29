package com.sportscenter.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "instructors")
public class InstructorEntity extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "picture_url")
    private String pictureUrl;

    @OneToMany(
            mappedBy = "instructor",
            targetEntity = SportClassEntity.class,
            cascade = CascadeType.ALL)
    private List<SportClassEntity> sportClasses;
}
