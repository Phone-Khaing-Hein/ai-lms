package com.ai.service;

import com.ai.entity.Video;
import com.ai.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileService fileService;

    public int findVideoCount(int moduleId) {
        return videoRepository.countByModuleId(moduleId);
    }

    public List<Video> findByModuleId(int moduleId) {
        return videoRepository.findByModuleId(moduleId);
    }

    public void save(MultipartFile video, Video v, int moduleId) throws IOException {
        fileService.createFile(video, "\\"+v.getModule().getCourse().getName().trim()+"\\"+v.getModule().getName().trim().concat("\\videos"));
        videoRepository.save(v);
    }

    public Video findById(int videoId) {
        return videoRepository.findById(videoId).get();
    }

    public void deleteById(Video video, String moduleName, String courseName) {
        var courseFolderPath = new File("src\\main\\resources\\static\\courses\\").getAbsolutePath();
        fileService.deleteFile(courseFolderPath.concat("\\"+courseName.concat("\\").concat(moduleName).concat("\\videos\\").concat(video.getName())));
        videoRepository.deleteById(video.getId());
    }
}
