package hello.service;

import hello.entity.User;
import hello.dao.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;
    @InjectMocks
    UserService userService;

    @Test
    void testSave(){

        // given 给定一个条件
        when(mockEncoder.encode("myPassword")).thenReturn("myEncryptedPassword");
       // 执行测试的操作
        userService.save("myUser","myPassword");
       // 然后会执行
        verify(mockMapper).save("myUser","myEncryptedPassword");
    }

    @Test
    void testGetUserByUsername(){
        userService.getUserByUsername("myUser");
        verify(mockMapper).findUserByUsername("myUser");
    }

    @Test
    void throwExceptionWhenUserNotFound(){
        when(mockMapper.findUserByUsername("myUser")).thenReturn(null);

        // 执行了后面的函数，断言会报异常
        Assertions.assertThrows(UsernameNotFoundException.class,
                ()->userService.loadUserByUsername("myUser"));
    }

    @Test
    void returnUserServiceWhenUserFound(){
        when(mockMapper.findUserByUsername("myUser"))
                .thenReturn(new User(123,"myUser","myEncodedPassword"));

        UserDetails userDetails = userService.loadUserByUsername("myUser");

        Assertions.assertEquals("myUser",userDetails.getUsername());
        Assertions.assertEquals("myEncodedPassword",userDetails.getPassword());

    }



}