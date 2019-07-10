package hello.service;

public class UserService {

    public User getUserById(Integer id){
        return new User(id,"");
    }
}
