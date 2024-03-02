package com.sportscenter.repository;

import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.enums.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findAllByUserAndStatus(UserEntity user, BookingStatusEnum status);

    List<BookingEntity> findAllByUser(UserEntity user);

    List<BookingEntity> findBySportClassDayOfWeekAndStatus(DayOfWeek dayOfWeek, BookingStatusEnum active);

    List<BookingEntity> findByStatusNot(BookingStatusEnum active);
}
