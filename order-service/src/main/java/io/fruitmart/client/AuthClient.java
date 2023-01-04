package io.fruitmart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("auth-service")
public interface AuthClient {
	
	@GetMapping("/api/auth/user")
	public String getData();
}
