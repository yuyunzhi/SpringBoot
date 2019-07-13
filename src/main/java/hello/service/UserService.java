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
    private Map<String,String> userPasswords = new ConcurrentHashMap<>();

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        save("gebilaowang","gebilaowang");
    }

    public void save(String username, String password){
        userPasswords.put(username,bCryptPasswordEncoder.encode(password));
    }

    public String getPassword(String username){
        return userPasswords.get(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(!userPasswords.containsKey(username)){
           throw new UsernameNotFoundException(username+"不存在");

       }
       
       String encodedPassword = userPasswords.get(username);

       return new org.springframework.security.core.userdetails.User(username,encodedPassword, Collections.emptyList());
    }

    public User getUserById(Integer id){
        return null;
    }
}
