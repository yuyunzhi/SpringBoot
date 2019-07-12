package hello.controller;

import hello.entity.Result;
import hello.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }


    @GetMapping("/auth")
    @ResponseBody
    public Result auth(){
        return new Result("ok","登录成功",true);
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String,Object> usernameAndPasswordJson){

        String username = usernameAndPasswordJson.get("username").toString();
        String password = usernameAndPasswordJson.get("password").toString();

        UserDetails userDetails = null;
        try{
            System.out.println(1);
            userDetails = userDetailsService.loadUserByUsername(username);

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
            User loggedInUser = new User(1,"张三");
            return new Result("ok","登录成功",true,loggedInUser);
        }catch (BadCredentialsException e){
            System.out.println("error");
           return new Result("fail","密码不正确",false);

        }
    }


}
