package com.ai.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ai.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{

    @Query("select a from Attendance a where a.date=?1")
    Attendance findByDate(LocalDate date);

    @Query("SELECT a FROM Attendance a JOIN a.batch b where b.id = :batch_id")
    List<Attendance> findAllAttendanceByBatchId(int batch_id);
    
}
