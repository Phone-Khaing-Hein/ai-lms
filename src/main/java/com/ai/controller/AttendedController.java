package com.ai.controller;

import com.ai.entity.Attendance;
import com.ai.entity.Batch;
import com.ai.entity.User;
import com.ai.service.AttendanceService;
import com.ai.service.BatchService;
import com.ai.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AttendedController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    @GetMapping("/create-attendance/{id}")
    public String createAttendance(@PathVariable int id, Model model) {
        Batch batch = batchService.findById(id);
        List<User> students = batch.getUsers().stream().filter(user -> user.getRole().equals(User.Role.Student))
                .collect(Collectors.toList());
        model.addAttribute("students", students);
        model.addAttribute("batch", batch);
        model.addAttribute("attendance", new Attendance());
        return "/teacher/TCH-AT001";
    }

    @PostMapping("/save-attendance/{id}")
    public String createAttendance(@PathVariable int id, @ModelAttribute Attendance attendance,
                                   BindingResult bs, ModelMap map, RedirectAttributes attributes) {

        String attendanceStatus = attendance.getStatus();
        String[] attendanceStatusBYOne = attendanceStatus.split(",");

        if (bs.hasErrors()) {
            return "/teacher/TCH-AT001";
        } else {

            Boolean checkError = false;
            List<Attendance> allAttendance = attendanceService.findAllAttendance();
            for (Attendance attendance1 : allAttendance) {

                if (attendance1.getDate().compareTo(attendance.getDate()) == 0) {

                    System.out.println(attendance1.getDate());
                    Batch batch = attendance1.getBatch();
                    attributes.addFlashAttribute("batch", batch);
                    attributes.addFlashAttribute("error", "Attendance already created for the date you chose!!");
                    checkError = true;
                    return "redirect:/create-attendance/" + id;

                }
            }

            if (!checkError) {
                attendance.setBatch(batchService.findById(id));
                List<User> students = batchService.findById(id).getUsers().stream()
                        .filter(user -> user.getRole().equals(User.Role.Student)).collect(Collectors.toList());
                for (User student : students) {
                    attendance.setUser(student);
                    for (String attendanceStatusString : attendanceStatusBYOne) {
                        attendance.setStatus(attendanceStatusString);

                    }
                    attendanceService.save(attendance);
                }

            }
            return "redirect:/teacher/batch-detail?batchId=" + id + "#attendance-tab";
        }
    }

    @GetMapping("setupEditAttendance")
    public String editAttendance(@RequestParam("loginId") String loginId,
                                @RequestParam("batchId") int batchId, 
                                ModelMap m,
                                RedirectAttributes attributes){
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("loginId => " + loginId + " and batchId => " + batchId);
        User student = userService.findByLoginId(loginId);
        attributes.addFlashAttribute("student", student);
        System.out.println("Students => " + student.getLoginId());
        System.out.println("Students => " + student.getName());
        System.out.println("Students => " + student.getEmail());
        //System.out.println("Students => " + student.getAttendances().get(1));
        System.out.println("--------------------------------------------------------------------------");
        return "redirect:/teacher/batch-detail?batchId=" + batchId + "#attendance-tab";
    }
}
