package com.chen.crm.utils;

import org.springframework.stereotype.Component;

@Component
public class ServiceFactory {
	
	public static Object getService(Object service){
		
		return new TransactionInvocationHandler(service).getProxy();
		
	}
	
}
