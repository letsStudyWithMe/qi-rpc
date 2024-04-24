package com.qi.example.consumer;


import com.qi.example.common.model.User;
import com.qi.example.common.service.UserService;
import com.qi.qirpc.core.proxy.ServiceProxyFactory;

public class EasyConsumerExample {

	public static void main(String[] args) {
		UserService userService = ServiceProxyFactory.getProxy(UserService.class);
		User user = new User();
		user.setName("qiiiiiiiii");

		User newUser = userService.getUser(user);
		if (newUser != null){
			System.out.println(newUser.getName());
		}else {
			System.out.println("newUser is null");
		}
	}

}
