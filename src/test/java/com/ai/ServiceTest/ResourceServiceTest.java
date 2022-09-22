package com.ai.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.entity.Course;
import com.ai.entity.Module;
import com.ai.entity.Resource;
import com.ai.entity.Schedule;
import com.ai.repository.ResourceRepository;
import com.ai.service.FileService;
import com.ai.service.ResourceService;

@SpringBootTest
public class ResourceServiceTest {
    
    @Mock
    ResourceRepository resourceRepository;

    @InjectMocks
    ResourceService resourceService;

    @InjectMocks
    FileService fileService;

    public Resource resource(){
        Resource resource = Resource.builder()
        .id(1)
        .name("Resource Object")
        .module(moduleList().get(0))
        .build();
        return resource;
    }

    public List<Resource> resources(){
        List<Resource> resources = new ArrayList<Resource>();

        Resource resource1 = Resource.builder()
        .id(1)
        .name("Resource Object 1")
        .module(moduleList().get(0))
        .build();

        Resource resource2 = Resource.builder()
        .id(2)
        .name("Resource Object 2")
        .module(moduleList().get(1))
        .build();

        resources.add(resource1);
        resources.add(resource2);
        return resources;
    }

    public List<Module> moduleList(){
        List<Module> modules = new ArrayList<Module>();

        Module module1 = Module.builder()
        .id(1)
        .name("Module Name 1")
        .course(courseObj())
        .resources(new ArrayList<>())
        .videos(new ArrayList<>())
        .schedule(new Schedule())
        .resourceCount(2)
        .videoCount(2)
        .build();

        Module module2 = Module.builder()
        .id(2)
        .name("Module Name 2")
        .course(courseObj())
        .resources(new ArrayList<>())
        .videos(new ArrayList<>())
        .schedule(new Schedule())
        .resourceCount(2)
        .videoCount(2)
        .build();
        
        modules.add(module1);
        modules.add(module2);
        
        return modules;
    }

    public Course courseObj(){
        Course course = Course.builder()
        .id(1)
        .name("OJT")
        .description("description")
        .build();
        return course;
    }

    // @Test
    // public void saveTest(){
    //     String fileName = "sampleFile.txt";
    //     MockMultipartFile sampleFile = new MockMultipartFile(
    //     "uploaded-file",
    //     fileName,
    //     "text/plain",
    //     "This is the file content".getBytes()
    // );
    //     Resource resource = resource();
    //     try {
    //         resourceService.save(sampleFile, resource, 1);
    //     } catch (IOException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    //     verify(resourceRepository, times(1)).save(resource);
    // }

    @Test
    public void findByIdTest(){
        Resource resource = resource();
        Mockito.when(resourceRepository.findById(1)).thenReturn(Optional.of(resource));
        Resource getResource = resourceService.findById(1);
        assertEquals("Resource Object", getResource.getName());
    }

    // @Test
    // public void deleteByIdTest(){
    //     String moduleName = "moduleName";
    //     String courseName = "courseName";
    //     var courseFolderPath = new File("src\\main\\resources\\static\\courses\\").getAbsolutePath();
    //     fileService.deleteFile(((String) courseFolderPath).concat("\\"+courseName.concat("\\").concat(moduleName).concat("\\resources\\").concat("Resource Object")));
    //     resourceService.deleteById(resource(), moduleName, courseName);
    //     verify(resourceRepository, times(1)).deleteById(1);
    // }

    @Test
    public void findByModuleIdTest(){
        List<Resource> resources = resources();
        Mockito.when(resourceRepository.findByModuleId(1)).thenReturn(resources);
        List<Resource> resourceList = resourceService.findByModuleId(1);
        assertEquals(2, resourceList.size());
        verify(resourceRepository, times(1)).findByModuleId(1);
    }

    @Test
    public void findResourceCountTest(){
        int serviceCount = resourceService.findResourceCount(1);
        int repositoryCount = resourceRepository.countByModuleId(1);
        assertEquals(serviceCount, repositoryCount);
    }
}
