package com.ai.ServiceTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Attendance;
import com.ai.entity.Batch;
import com.ai.entity.User;
import com.ai.entity.User.Role;
import com.ai.repository.AttendanceRepository;
import com.ai.service.AttendanceService;

@SpringBootTest
public class AttendanceServiceTest {
    
    @Mock
    AttendanceRepository attendanceRepository;

    @InjectMocks
    AttendanceService attendanceService;

    public Batch batchObj(){
        Batch batch = Batch.builder()
        .id(1)
        .name("Batch-1")
        .startDate(LocalDate.of(2022, 9, 23))
        .endDate(LocalDate.of(2022, 12, 6))
        .build();
        return batch;
    }

    public User userObj(){
        User user = User.builder()
        .loginId("ADM001")
        .name("Admin User")
        .password("admin")
        .role(Role.Admin)
        .photo("photo.png")
        .email("phyuthin2004@gmail.com")
        .isActive(true)
        .build();
        return user;
    }

    public Attendance attendanceObj(){
        Attendance attendance = Attendance.builder()
        .id(1)
        .attendanceStatus("Present")
        .date(LocalDate.of(2022, 12, 6))
        .batch(batchObj())
        .user(userObj())
        .build();
        return attendance;
    }
    @Test
    public void saveAttendanceTest(){
        Attendance attendance = attendanceObj();
        attendanceService.save(attendance);
        verify(attendanceRepository, times(1)).save(attendance);
    }
}
