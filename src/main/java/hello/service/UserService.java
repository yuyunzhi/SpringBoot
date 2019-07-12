package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
//    private UserMapper userMapper;
    private Map<String,String> userPasswords = new ConcurrentHashMap<>();


    public void save(String username,String password){
        userPasswords.put(username,password);
    }

    public String getPassword(String username){
        return userPasswords.get(username);
    }
//    @Inject
//    public UserService(UserMapper userMapper) {
//        this.userMapper = userMapper;
//    }
//
//    public User getUserById(Integer id){
//        return userMapper.findUserById(id);
//    }
}
