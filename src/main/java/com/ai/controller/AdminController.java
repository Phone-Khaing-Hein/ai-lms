package com.ai.controller;

import com.ai.entity.*;
import com.ai.entity.Module;
import com.ai.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin")
public class AdminController {

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
    public String home(ModelMap m){
        m.put("courseCount", courseService.getCount());
        m.put("batchCount", batchService.getCount());
        List<String> batchName = batchService.findAll().stream().map(batch -> batch.getName()).collect(Collectors.toList());
        List<Batch> batches = batchService.findAll().stream().filter(batch -> batch.isClose() == false).collect(Collectors.toList());

        List<Integer> sizeList=new ArrayList<>();

        for (Batch batch:
             batches) {
            List<User> u = batch.getUsers()
                    .stream()
                    .filter(user -> user.getRole().equals(User.Role.Student))
                    .collect(Collectors.toList());
            sizeList.add(u.size());
        }
        m.addAttribute("studentCount",
                sizeList);
        m.addAttribute("batchName",batchName);
//        System.out.println(batchName);
        return "ADM-DB001";
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

    @PostMapping("module-create")
    public String moduleCreate(@Validated @ModelAttribute Module module, BindingResult bs,
                               @RequestParam int courseId,
                               RedirectAttributes attr, ModelMap map) throws IllegalStateException, IOException {

        if(bs.hasErrors()) {
            return "ADM-MT001";
        }
        var m = moduleService.findByName(module.getName());
        if(m != null) {
            attr.addFlashAttribute("error", "%s module has already existed!".formatted(module.getName()));
            return "redirect:/admin/course-detail?courseId=%d".formatted(courseId);
        }
        module.setCourse(courseService.findById(courseId));
        moduleService.save(module);

        attr.addFlashAttribute("message", "%s module created successfully!".formatted(module.getName()));
        return "redirect:/admin/course-detail?courseId=%d".formatted(courseId);
    }

    @GetMapping("resource-list")
    public String resourceList(@RequestParam int moduleId, ModelMap m){
        var resources = resourceService.findByModuleId(moduleId);
        m.put("resources", resources);
        var module = moduleService.findById(moduleId);
        m.put("moduleName", module.getName());
        m.put("course", module.getCourse());
        return "ADM-RS001";
    }

    @PostMapping("module-edit")
    public String moduleEdit(@RequestParam int moduleId, @RequestParam String name, @RequestParam int courseId) throws IOException {
        var m = moduleService.findById(moduleId);
        var courseName = courseService.findById(courseId).getName();
        fileService.renameFolder(courseName.concat("\\").concat(m.getName()), name);
        m.setName(name);
        m.setVideo(new MultipartFile[]{});
        m.setResource(new MultipartFile[]{});
        moduleService.edit(m);
        return "redirect:/admin/course-detail?courseId=%d".formatted(courseId);
    }

    @GetMapping("module-delete")
    public String deleteCourse(@RequestParam int moduleId, RedirectAttributes attr)
            throws IOException {
        var m = moduleService.findById(moduleId);
        var courseId = m.getCourse().getId();
        moduleService.deleteById(moduleId, m.getName(), courseId);
        attr.addFlashAttribute("message", "%s module deleted successfully!".formatted(m.getName()));
        return "redirect:/admin/course-detail?courseId=%d".formatted(courseId);
    }

    @GetMapping("resource-delete")
    public String deleteResource(@RequestParam int resourceId, RedirectAttributes attr){
        var resource = resourceService.findById(resourceId);
        var moduleName = resource.getModule().getName();
        var courseName = resource.getModule().getCourse().getName();
        resourceService.deleteById(resource, moduleName, courseName);
        attr.addFlashAttribute("message", "Resource deleted successfully!");
        return "redirect:/admin/resource-list?moduleId=%d".formatted(resource.getModule().getId());
    }

    @PostMapping("resource-create")
    public String createResource(@RequestParam MultipartFile resource, @RequestParam int moduleId) throws IOException {
        var r = new Resource();
        r.setName(resource.getOriginalFilename());
        r.setModule(moduleService.findById(moduleId));
        resourceService.save(resource, r, moduleId);
        return "redirect:/admin/resource-list?moduleId=%d".formatted(moduleId);
    }

    @GetMapping("video-list")
    public String videoList(@RequestParam int moduleId, ModelMap m){
        var videos = videoService.findByModuleId(moduleId);
        m.put("videos", videos);
        var module = moduleService.findById(moduleId);
        m.put("moduleName", module.getName());
        m.put("course", module.getCourse());
        return "ADM-VD001";
    }

    @PostMapping("video-create")
    public String createVideo(@RequestParam MultipartFile video, @RequestParam int moduleId) throws IOException {
        var v = new Video();
        v.setName(video.getOriginalFilename());
        v.setModule(moduleService.findById(moduleId));
        videoService.save(video, v, moduleId);
        return "redirect:/admin/video-list?moduleId=%d".formatted(moduleId);
    }

    @GetMapping("video-delete")
    public String deleteVideo(@RequestParam int videoId, RedirectAttributes attr){
        var video = videoService.findById(videoId);
        var moduleName = video.getModule().getName();
        var courseName = video.getModule().getCourse().getName();
        videoService.deleteById(video, moduleName, courseName);
        attr.addFlashAttribute("message", "Resource deleted successfully!");
        return "redirect:/admin/video-list?moduleId=%d".formatted(video.getModule().getId());
    }

    @GetMapping("batch-list")
    public String batchList(){
        return "ADM-BT001";
    }

    @PostMapping("batch-create")
    public String createBatch(@Validated @ModelAttribute Batch batch, BindingResult bs, RedirectAttributes attributes){
        if(bs.hasErrors()){
            return "ADM-BT001";
        }

        var b = batchService.findByName(batch.getName());
        if(b != null){
            attributes.addFlashAttribute("error", "Batch Name has already existed!");
            return "redirect:/admin/batch-list";
        }
        if(!batch.getStartDate().isBefore(batch.getEndDate())){
            attributes.addFlashAttribute("error", "Start Date should be earlier than End Date!");
            return "redirect:/admin/batch-list";
        }
        batch.setClose(false);
        batchService.save(batch);
        attributes.addFlashAttribute("message", "%s created successfully!".formatted(batch.getName()));
        return "redirect:/admin/batch-list";
    }

    @PostMapping("batch-edit")
    public String batchEdit(@ModelAttribute Batch batch, RedirectAttributes attributes) throws IOException {
        var b = batchService.findById(batch.getId());
        b.setCourse(courseService.findById(batch.getCourse().getId()));
        b.setName(batch.getName());
        b.setStartDate(batch.getStartDate());
        b.setEndDate(batch.getEndDate());
        batchService.save(b);
        attributes.addFlashAttribute("message", "%s deleted successfully!".formatted(batch.getName()));
        return "redirect:/admin/batch-list";
    }

    @GetMapping("batch-delete")
    public String batchDelete(@RequestParam int batchId, @RequestParam String batchName, RedirectAttributes attributes){
        batchService.deleteById(batchId);
        attributes.addFlashAttribute("message", "%s updated successfully!".formatted(batchName));
        return "redirect:/admin/batch-list";
    }

    @GetMapping("batch-close")
    @Transactional
    public String closeBatch(@RequestParam int id){
        var batch = batchService.findById(id);
        batch.setClose(true);
        for(var student : batch.getUsers()){
            student.setBatchId(new Integer[]{student.getBatches().get(0).getId()});
            student.setActive(false);
        }
        batchService.save(batch);
        return "redirect:/admin/batch-list";
    }

    @GetMapping("batch-open")
    @Transactional
    public String openBatch(@RequestParam int id){
        var batch = batchService.findById(id);
        batch.setClose(false);
        for(var student : batch.getUsers()){
            student.setBatchId(new Integer[]{student.getBatches().get(0).getId()});
            student.setActive(true);
        }
        batchService.save(batch);
        return "redirect:/admin/batch-list";
    }

    @PostMapping("student-create")
    public String createStudent(@Validated @ModelAttribute("student") User user, BindingResult bs, RedirectAttributes attributes, ModelMap m){
        if(bs.hasErrors()){
            m.put("openBatches", batchService.findAll().stream().filter(b -> b.isClose() == false).toList());
            return "ADM-ST001";
        }
        var students = userService.findAll().stream().filter(a -> a.getRole().equals(User.Role.Student)).toList();
        for(var s : students){
            if(s.getLoginId().equals(user.getLoginId())){
                m.put("error" , "Student Login ID has already existed!");
                m.put("openBatches", batchService.findAll().stream().filter(b -> b.isClose() == false).toList());
                return "ADM-ST001";
            }
            if(s.getEmail().equals(user.getEmail())){
                m.put("emailError" , "Student Email has already existed!");
                m.put("openBatches", batchService.findAll().stream().filter(b -> b.isClose() == false).toList());
                return "ADM-ST001";
            }
        }
        var batches = batchService.findAll();
        for(var batch : batches){
            if(batch.getId() == user.getBatchId()[0] && batch.isClose()){
                m.put("batchError" , "%s has already closed!".formatted(batch.getName()));
                return "ADM-ST001";
            }
        }

        user.setPassword(passwordEncoder.encode(user.getEmail()));
        var batch = batchService.findById(user.getBatchId()[0]);
        user.setBatches(new ArrayList<>(List.of(batch)));
        userService.save(user);
        attributes.addFlashAttribute("message", "%s added successfully!".formatted(user.getName()));
        return "redirect:/admin/student-list";
    }

    @GetMapping("student-list")
    public String studentList(ModelMap m){
        m.put("openBatches", batchService.findAll().stream().filter(b -> b.isClose() == false).toList());
        return "ADM-ST001";
    }

    @PostMapping("student-edit")
    public String studentEdit(@ModelAttribute User user, RedirectAttributes attributes){
        var student = userService.findByLoginId(user.getLoginId());
        student.setEmail(user.getEmail());
        student.setBatches(new ArrayList<>(List.of(batchService.findById(user.getBatchId()[0]))));
        student.setName(user.getName());
        userService.save(student);
        attributes.addFlashAttribute("message", "%s updated successfully!".formatted(student.getName()));
        return "redirect:/admin/student-list";
    }

    @GetMapping("student-delete")
    public String studentDelete(@RequestParam String studentId, @RequestParam String studentName, RedirectAttributes attributes){
        userService.deleteById(studentId);
        attributes.addFlashAttribute("message", "%s deleted successfully!".formatted(studentName));
        return "redirect:/admin/student-list";
    }

    @ModelAttribute("students")
    public List<User> users(){return userService.findAll().stream().filter(a -> a.getRole().equals(User.Role.Student)).toList();};

    @ModelAttribute("student")
    public User student(){
        var student = new User();
        student.setRole(User.Role.Student);
        student.setActive(true);
        return student;
    }

    @ModelAttribute("batches")
    public List<Batch> batches(){return batchService.findAll();};

    @ModelAttribute("courses")
    public List<Course> courses() {
        return courseService.findAll();
    }

    @ModelAttribute("course")
    public Course course() {
        return new Course();
    }

    @ModelAttribute("module")
    public Module module() {
        return new Module();
    }

    @ModelAttribute("batch")
    public Batch batch(){return new Batch();};

    @ModelAttribute("teacher")
    public User user() {
        return new User();
    }


    @PostMapping("teacher-create")
    public String postCreate(@Validated @ModelAttribute User user, BindingResult bs, RedirectAttributes attr, ModelMap m){
        if(bs.hasErrors()){
            return "ADM-TC001";
        }
        var u = userService.findByLoginId(user.getLoginId());
        if(u != null) {
            attr.addFlashAttribute("error", "%s has already existed!".formatted(user.getLoginId()));
            return "redirect:/admin/teacher-list";
        }
        user.setRole(User.Role.Teacher);
        user.setPassword(passwordEncoder.encode(user.getEmail()));
        var batches = new ArrayList<Batch>();
        for(var id : user.getBatchId()){
            batches.add(batchService.findById(id));
        }
        user.setBatches(batches);
        user.setActive(true);
        userService.save(user);
        attr.addFlashAttribute("message", "%s created successfully!".formatted(user.getName()));

        return "redirect:/admin/teacher-list";
    }

    @GetMapping("teacher-list")
    public String teacherList(ModelMap m) {
        var teachers = userService.findAll().stream().filter(u -> u.getRole().equals(User.Role.Teacher)).toList();
        m.put("openBatches", batchService.findAll().stream().filter(b -> b.isClose() == false).toList());
        m.put("teachers", teachers);
        return "ADM-TC001";
    }
}
