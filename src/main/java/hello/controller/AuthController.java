package hello.controller;

import hello.entity.Result;
import hello.entity.User;
import hello.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {

    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @GetMapping("/auth")
    @ResponseBody
    public Result auth(){

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("userName = " + userName);
        User loggedInUser = userService.getUserByUsername(userName);

        if(loggedInUser==null){
            return new Result("ok","用户没有登录",false);
        }else{
            return new Result("ok",null,true,userService.getUserByUsername(userName));

        }
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String,Object> usernameAndPasswordJson){

        String username = usernameAndPasswordJson.get("username").toString();
        String password = usernameAndPasswordJson.get("password").toString();

        UserDetails userDetails = null;
        try{
            System.out.println(1);
            userDetails = userService.loadUserByUsername(username);

        }catch(UsernameNotFoundException e){
            return new Result("fail","用户不存在",false);

        }
        System.out.println(2);
        UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());

        try{
            //判断是否是同一个人
            authenticationManager.authenticate(token);
            // 设置cookie
            SecurityContextHolder.getContext().setAuthentication(token);
            // 返回前端所需要的数据
            return new Result("ok","登录成功",true,userService.getUserByUsername(username));
        }catch (BadCredentialsException e){
            System.out.println("error");
           return new Result("fail","密码不正确",false);

        }
    }


}
