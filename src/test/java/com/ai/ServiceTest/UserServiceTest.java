package com.ai.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.repository.UserRepository;
import com.ai.service.UserService;
import com.ai.entity.User;
import com.ai.entity.User.Role;

@SpringBootTest
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    
    @InjectMocks
    UserService userService;

    public User userObj(){
        User user = User.builder()
        .loginId("ADM001")
        .name("Admin User")
        .password("admin")
        .role(Role.Admin)
        .email("phyuthin2004@gmail.com")
        .isActive(true)
        .build();
        return user;
    }

    public List<User> userList(){
        List<User> user = new ArrayList<User>();
        User student = User.builder()
        .loginId("STU001")
        .name("Phyu Phyu Thin")
        .password("asdfqwer")
        .role(Role.Student)
        .email("phyuthin2004@gmail.com")
        .isActive(true)
        .build();

        User admin = User.builder()
        .loginId("ADM001")
        .name("Admin User")
        .password("admin")
        .role(Role.Admin)
        .email("phyuthin2004@gmail.com")
        .isActive(true)
        .build();

        User teacher = User.builder()
        .loginId("TCH001")
        .name("Phyu Phyu Thin")
        .password("asdfqwer")
        .role(Role.Teacher)
        .email("phyuthin2004@gmail.com")
        .isActive(true)
        .build();

        user.add(student);
        user.add(admin);
        user.add(teacher);

        return user;
    }

    @Test
    public void saveUserTest(){
        User user = userObj();
        userService.save(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void findAllUserTest(){
        List<User> user = userList();
        Mockito.when(userRepository.findAll()).thenReturn(user);
        List<User> userList = userService.findAll();
        assertEquals(3, userList.size());
        verify(userRepository, times(1)).findAll();
    }
//    .loginId("ADM001")
//    .name("Admin User")
//    .password("admin")
//    .role(Role.Admin)
//    .email("phyuthin2004@gmail.com")
//    .isActive(true)
   @Test
   public void findByLoginIdTest(){
       User user = userObj();
       Mockito.when(userRepository.findByLoginId("ADM001")).thenReturn((user));
       User getUser = userService.findByLoginId("ADM001");
       assertEquals("Admin User", getUser.getName());
       assertEquals("admin", getUser.getPassword());
       assertEquals(Role.Admin, getUser.getRole());
       assertEquals("phyuthin2004@gmail.com", getUser.getEmail());
       assertEquals(true, getUser.isActive);
   }

    @Test
    public void findUserByTeacherRoleTest(){
        List<User> teacher = userList().stream().filter(a -> a.getRole().equals(User.Role.Teacher)).toList();
        assertEquals(teacher.size(), 1);
    }
    
    @Test
	public void deleteTest() {
		userService.deleteById("STU001");
		verify(userRepository,times(1)).deleteById("STU001");
	}

    @Test
    public void getCountTest(){
        int serviceCount = userService.getCount();
        int repositoryCount = (int) userRepository.count();
        assertEquals(serviceCount, repositoryCount);
    }
}
