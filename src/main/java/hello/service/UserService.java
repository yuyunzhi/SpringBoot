package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import java.util.Collections;


public class UserService implements UserDetailsService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserMapper userMapper;

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserMapper userMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper=userMapper;
        //save("user","user");
    }

    public void save(String username, String password){
        userMapper.save(username,bCryptPasswordEncoder.encode(password));
 }

    public User getUserByUsername(String username){

        return userMapper.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getUserByUsername(username);
        if(user==null){
           throw new UsernameNotFoundException(username+"不存在");

       }

       return new org.springframework.security.core.userdetails.User(username,user.getEncryptedPassword(), Collections.emptyList());
    }

//    public User getUserById(Integer id){
//        return null;
//    }


}
