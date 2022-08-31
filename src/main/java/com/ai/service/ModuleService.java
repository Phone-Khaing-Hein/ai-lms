package com.ai.service;

import com.ai.entity.Module;
import com.ai.entity.Resource;
import com.ai.entity.Video;
import com.ai.repository.ModuleRepository;
import com.ai.repository.ResourceRepository;
import com.ai.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository repository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private CourseService courseService;

    public List<Module> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void save(Module module) throws IOException {
        fileService.createFolderForModule(module.getCourse().getName().trim().concat("\\".concat(module.getName().trim())));
        var m = repository.save(module);
        for(var resource : m.getResource()){
            fileService.createFile(resource, "\\"+module.getCourse().getName()+"\\"+module.getName().trim().concat("\\resources"));
            var resourceEntity = new Resource();
            resourceEntity.setModule(m);
            resourceEntity.setName(resource.getOriginalFilename());
            resourceRepository.save(resourceEntity);
        }
        for(var video : m.getVideo()){
            fileService.createFile(video, "\\"+module.getCourse().getName()+"\\"+module.getName().trim().concat("\\videos"));
            var videoEntity = new Video();
            videoEntity.setModule(m);
            videoEntity.setName(video.getOriginalFilename());
            videoRepository.save(videoEntity);
        }
    }

    public List<Module> findByCourseId(int courseId) {
        return repository.findByCourseId(courseId);
    }

    public Module findByName(String name) {
        return repository.findByName(name);
    }

    public Module findById(int moduleId) {
        return repository.findById(moduleId).get();
    }

    public void edit(Module m){
        repository.save(m);
    }

    public void deleteById(int moduleId, String moduleName, int courseId) throws IOException {
        var courseName = courseService.findById(courseId).getName();
        fileService.deleteFolder(courseName.concat("\\").concat(moduleName));
        repository.deleteById(moduleId);
    }
}
