package com.ai.controller;

import com.ai.entity.Course;
import com.ai.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("teacher")
public class TeacherController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private FileService fileService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("home")
    public String home(){
        return "teacher/TCH-DB001";
    }

    @GetMapping("batch-list")
    public String batchList(ModelMap m){
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var batches = userService.findByLoginId(loginId).getBatches();
        m.put("batches", batches);
        return "ADM-BT001";
    }

    @GetMapping("course-list")
    public String courseList(){
        return "ADM-CT001";
    }

    @PostMapping("course-create")
    public String postCreate(@Validated @ModelAttribute Course course, BindingResult bs, RedirectAttributes attr, ModelMap m){
        if(bs.hasErrors()){
            return "ADM-CT001";
        }
        var c = courseService.findByName(course.getName());
        if(c != null) {
            attr.addFlashAttribute("error", "%s course has already existed!".formatted(course.getName()));
            return "redirect:/admin/course-list";
        }
        courseService.save(course);
        attr.addFlashAttribute("cmessage", "%s course created successfully!".formatted(course.getName()));
        return "redirect:/admin/course-list";
    }

    @GetMapping("course-detail")
    public String detail(ModelMap m, @RequestParam int courseId) {
        var modules = moduleService.findByCourseId(courseId);
        for(var module : modules) {
            var resourceCount = resourceService.findResourceCount(module.getId());
            var videoCount = videoService.findVideoCount(module.getId());
            module.setResourceCount(resourceCount);
            module.setVideoCount(videoCount);
        }
        m.put("modules", modules);
        m.put("courseName", courseService.findById(courseId).getName());
        return "ADM-MT001";
    }

    @PostMapping("/course-edit")
    public String editCourse(@RequestParam int id,
                             @RequestParam String name,
                             @RequestParam String description,
                             RedirectAttributes redirectAttrs) throws IllegalStateException, IOException {

        var course = courseService.findById(id);

        fileService.renameFolder(course.getName(), name);

        course.setName(name);
        course.setDescription(description);
        courseService.save(course);

        return "redirect:/admin/course-list";
    }

    @GetMapping("course-delete")
    public String deleteCourse(@RequestParam int courseId, @RequestParam String courseName)
            throws IOException {
        courseService.deleteById(courseId, courseName);
        return "redirect:/admin/course-list";
    }

    @ModelAttribute("courses")
    public List<Course> courses() {
        return courseService.findAll();
    }

    @ModelAttribute("course")
    public Course course() {
        return new Course();
    }
}
