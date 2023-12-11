package com.sportscenter.repository;

import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.view.SportClassViewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface SportClassRepository extends JpaRepository<SportClassEntity, Long> {

    List<SportClassEntity> findByDayOfWeek(DayOfWeek dayOfWeek);

}
