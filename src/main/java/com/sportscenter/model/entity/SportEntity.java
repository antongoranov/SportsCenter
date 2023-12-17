package com.sportscenter.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sports")
public class SportEntity extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "sport_image_url")
    private String sportImageUrl;

    //edit
    @OneToMany(
            targetEntity = InstructorEntity.class,
            mappedBy = "sport", cascade = CascadeType.ALL) //added cascade property due to the tests
    private Set<InstructorEntity> instructors;

}
