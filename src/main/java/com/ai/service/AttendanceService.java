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

    public Attendance findById(int id){
        return attendanceRepository.findById(id);
    }
    
    public List<Attendance> findAllAttendance(){
        return attendanceRepository.findAll();
    }

    public int count() {
        return (int) attendanceRepository.count();
    }

    public List<Attendance> findAllAttendanceByBatchId(int batch_id){
        return attendanceRepository.findAllAttendanceByBatchId(batch_id);
    }
}
