package com.ai.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.entity.Attendance;
import com.ai.repository.AttendanceRepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public void save(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    // public Attendance findByDate(LocalDate date){
    //     return attendanceRepository.findByDate(date);
    // }
    
}
