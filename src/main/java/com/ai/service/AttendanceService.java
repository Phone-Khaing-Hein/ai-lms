package com.ai.service;

import com.ai.entity.Attendance;
import com.ai.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public void save(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    public List<Attendance> findAllAttendance(){
        return attendanceRepository.findAll();
    }

    public Object findAll() {
        return null;
    }
}
