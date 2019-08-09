package hello.controller;

import hello.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mvc;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @BeforeEach
    void setUp(){
        // 告诉MVC 只测试一个controller  AuthController
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(userService,authenticationManager)).build();
    }
    /**
     * 在JVM中的某个地方 每次调用测试用例都会生成一个新的实例去调用，从而使得各个用例不会有变量共享，如果下逻辑
     */
//    {
//        AuthControllerTest testAuthControllerTest= new AuthControllerTest();
//        testAuthControllerTest.setUp();
//        testAuthControllerTest.test1();
//
//
//        AuthControllerTest testAuthControllerTest1= new AuthControllerTest();
//        testAuthControllerTest1.setUp();
//        testAuthControllerTest1.test2();
//    }

    @Test
    void returnNotLoginByDefault() throws Exception{
        // 希望执行请求，并且返回成功的响应200  isOk() 表示200   并且拿到响应的结果，断言字符串包含了用户没有登录
        mvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> {
            //System.out.println(mvcResult.getResponse().getContentAsString());
            Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户没有登录"));
        });
    }

    @Test
    void testLogin(){

    }
}