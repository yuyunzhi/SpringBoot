package hello.service;

import javax.inject.Inject;

public class OrderService {

    private UserService userService;

    @Inject
    public OrderService(UserService userService){
        this.userService=userService;
    }

    public OrderService() { }

//    public void placeOrder(Integer userId,String item){
//        userService.getUserById(userId);
//    }
}
