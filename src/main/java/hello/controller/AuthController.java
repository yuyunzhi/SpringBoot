package hello.controller;

import hello.entity.Result;
import hello.entity.User;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        // 获取cookie
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication==null? null:authentication.getName();

        User loggedInUser = userService.getUserByUsername(userName);

        if(loggedInUser==null){
            return Result.success("用户没有登录");
        }else{
            return Result.successLogin(null,userService.getUserByUsername(userName));
        }
    }


    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedInUser = userService.getUserByUsername(username);

        if(loggedInUser==null){
            return Result.failure("用户没有登录");

        }else{
            SecurityContextHolder.clearContext();
            return Result.success("注销成功");
        }

    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String,String> usernameAndPasswordJson){
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");

        if(username == null || password == null){
            return Result.failure("密码或用户名为空");
        }

        if(username.length()<1 || username.length()>15){
            return Result.failure("用户名长度不符合规定");
        }

        if(password.length()<1 || password.length()>15){
            return Result.failure("密码长度不符合规定");

        }

        //  并发出现 潜在问题  所以不使用以下逻辑
//        User user = userService.getUserByUsername(username);
//
//        if(user==null){
//            userService.save(username,password);
//            return new Result("ok","注册成功",false);
//        }else{
//            return new Result("fail","用户已经存在",false);
//        }

        try{
            userService.save(username,password);
        }catch (DuplicateKeyException e){
            return Result.failure("用户已经存在");
        }
        return Result.success("注册成功");
    }


    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String,Object> usernameAndPasswordJson){

        String username = usernameAndPasswordJson.get("username").toString();
        String password = usernameAndPasswordJson.get("password").toString();

        UserDetails userDetails = null;
        try{
            userDetails = userService.loadUserByUsername(username);

        }catch(UsernameNotFoundException e){
            return Result.failure("用户不存在");
        }

        UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());

        try{
            //判断是否是同一个人
            authenticationManager.authenticate(token);
            // 设置cookie
            SecurityContextHolder.getContext().setAuthentication(token);
            // 返回前端所需要的数据
            return Result.successLogin("登录成功",userService.getUserByUsername(username));

        }catch (BadCredentialsException e){
            return Result.failure("密码不正确");

        }
    }


}
