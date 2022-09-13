package com.ai.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ai.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer>{

//    Attendance findByDate(LocalDate localDate);

    //Attendance findByDate(String date);
//
//    LocalDate findDistinctByDate(LocalDate date);

     @Query("select a from Attendance a where a.date=?1")
     Attendance findByDate(LocalDate date);
    
//    Attendance findDistinctByDate(LocalDate date);

    // @Query(value = "select distinct date from Attendance where date = ?1")
    // public String dateExists(String date);

    // // @Query(value = "select * from Attendance where batch_id=?1")
    // // public List<Attendance> selectAll(int batch_id);

}
