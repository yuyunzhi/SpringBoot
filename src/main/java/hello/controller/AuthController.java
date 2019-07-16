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
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("userName = " + userName);
        User loggedInUser = userService.getUserByUsername(userName);

        if(loggedInUser==null){
            return new Result("ok","用户没有登录",false);
        }else{
            return new Result("ok",null,true,userService.getUserByUsername(userName));

        }
    }


    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedInUser = userService.getUserByUsername(username);

        if(loggedInUser==null){
            return new Result("fail","用户没有登录",false);

        }else{
            SecurityContextHolder.clearContext();
            return new Result("ok","注销成功",false);
        }

    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String,String> usernameAndPasswordJson){
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");

        if(username == null || password == null){
            return new Result("fail","密码或用户名为空",false);
        }

        if(username.length()<1 || username.length()>15){
            return new Result("fail","用户名长度不符合规定",false);
        }

        if(password.length()<1 || password.length()>15){
            return new Result("fail","密码长度不符合规定",false);
        }

        User user = userService.getUserByUsername(username);

        if(user==null){
            userService.save(username,password);
            return new Result("ok","注册成功",false);
        }else{
            return new Result("fail","用户已经存在",false);
        }
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
            return new Result("fail","用户不存在",false);
        }

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
