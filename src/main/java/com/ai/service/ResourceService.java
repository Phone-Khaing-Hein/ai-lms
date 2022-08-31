package com.ai.service;

import com.ai.entity.Resource;
import com.ai.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private FileService fileService;

    public int findResourceCount(int moduleId) {
        return resourceRepository.countByModuleId(moduleId);
    }

    public List<Resource> findByModuleId(int moduleId) {
        return resourceRepository.findByModuleId(moduleId);
    }

    public Resource findById(int resourceId) {
        return resourceRepository.findById(resourceId).get();
    }

    public void deleteById(Resource resource, String moduleName, String courseName) {
        var courseFolderPath = new File("src\\main\\resources\\static\\courses\\").getAbsolutePath();
        fileService.deleteFile(courseFolderPath.concat("\\"+courseName.concat("\\").concat(moduleName).concat("\\resources\\").concat(resource.getName())));
        resourceRepository.deleteById(resource.getId());
    }

    public void save(MultipartFile r, Resource resource, int moduleId) throws IOException {
        fileService.createFile(r, "\\"+resource.getModule().getCourse().getName().trim()+"\\"+resource.getModule().getName().trim().concat("\\resources"));
        resourceRepository.save(resource);
    }
}
