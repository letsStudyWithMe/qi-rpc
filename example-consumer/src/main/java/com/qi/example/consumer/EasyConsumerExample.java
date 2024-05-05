package com.qi.example.consumer;


import com.qi.example.common.model.User;
import com.qi.example.common.service.UserService;
import com.qi.qirpc.core.proxy.ServiceProxyFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EasyConsumerExample {

	private static final Logger log = Logger.getLogger(EasyConsumerExample.class.getName());

	public static void main(String[] args) {
		UserService userService = ServiceProxyFactory.getProxy(UserService.class);
		User user = new User();
		user.setName("qiiiiiiiii");

		User newUser = userService.getUser(user);
		if (newUser != null){
			//使用占位符打印日志
			log.log(Level.INFO, "UserName: {0}",newUser.getName());
			//System.out.println("UserName: "+newUser.getName());
		}else {
			System.out.println("newUser is null");
		}
	}

}
