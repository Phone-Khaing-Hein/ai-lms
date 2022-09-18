package com.ai.api;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ai.service.FileService;
import com.ai.service.UserService;

@RestController
public class ProfileApi {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @PostMapping({"student/profile-change", "admin/profile-change", "teacher/profile-change"})
    public String profileChange(@RequestBody MultipartFile profile) throws IOException {
        var loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.findByLoginId(loginId);
        if(StringUtils.hasLength(user.getPhoto()) && !user.getPhoto().equals("default.png")){
            var profileFilePath = new File("src\\main\\resources\\static\\profile\\").getAbsolutePath();
            fileService.deleteFile(profileFilePath.concat("\\").concat(user.getPhoto()));
        }
        var fileName = fileService.createProfileFile(profile, user.getLoginId());
        user.setPhoto(fileName);
        userService.save(user);
        return "http://localhost:9090/resources/profile/%s".formatted(fileName);
    }
}
