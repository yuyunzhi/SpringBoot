package hello.service;

import hello.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService implements UserDetailsService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Map<String,User> users = new ConcurrentHashMap<>();

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        save("yyz123","yyz123");
    }

    public void save(String username, String password){
        users.put(username, new User(1,username,bCryptPasswordEncoder.encode(password)));
    }

    public User getUserByUsername(String username){
        return users.get(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(!users.containsKey(username)){
           throw new UsernameNotFoundException(username+"不存在");

       }
       
       User user = users.get(username);

       return new org.springframework.security.core.userdetails.User(username,user.getEncryptedPassword(), Collections.emptyList());
    }

//    public User getUserById(Integer id){
//        return null;
//    }


}
