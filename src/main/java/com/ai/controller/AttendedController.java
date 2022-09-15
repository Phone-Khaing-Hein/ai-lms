package com.ai.controller;

import com.ai.entity.Attendance;
import com.ai.entity.Batch;
import com.ai.entity.User;
import com.ai.service.AttendanceService;
import com.ai.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AttendedController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/create-attendance/{id}")
    public String createAttendance(@PathVariable int id, Model model) {
        Batch batch = batchService.findById(id);
        List<User> students = batch.getUsers().stream().filter(user -> user.getRole().equals(User.Role.Student)).collect(Collectors.toList());
        model.addAttribute("students", students);
        model.addAttribute("batch", batch);
        model.addAttribute("attendance", new Attendance());
        return "/teacher/TCH-AT001";
    }


    @PostMapping("/save-attendance/{id}")
    public String createAttendance(@PathVariable int id, @ModelAttribute Attendance attendance, BindingResult bs, ModelMap map) {

        String attendanceStatus = attendance.getAttendanceStatus();
        String[] attendanceStatusBYOne = attendanceStatus.split(",");

        if (bs.hasErrors()) {
            return "/teacher/TCH-AT001";
        } else {

            List<User> students = batchService.findById(id).getUsers().stream().filter(user -> user.getRole().equals(User.Role.Student)).collect(Collectors.toList());
            attendance.setBatch(batchService.findById(id));
            for (User student : students) {
                attendance.setUser(student);
                for (String attendanceStatusString : attendanceStatusBYOne) {
                    attendance.setAttendanceStatus(attendanceStatusString);
                }
            }
           attendanceService.save(attendance);
            return "redirect:/teacher/batch-detail?batchId=" + id + "#attendance-tab";
        }
    }
}
