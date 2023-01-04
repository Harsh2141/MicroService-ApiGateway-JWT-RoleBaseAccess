package io.fruitmart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import io.fruitmart.dto.AuthRequest;
import io.fruitmart.dto.AuthResponse;
import io.fruitmart.dto.UserVO;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
	private final JwtUtil jwt;

    @Autowired
    public AuthService(RestTemplate restTemplate,
                       final JwtUtil jwt) {
        this.restTemplate = restTemplate;
        this.jwt = jwt;
    }

    public AuthResponse register(AuthRequest authRequest) {
        //do validation if user already exists
//        authRequest.setPassword(BCrypt.hashpw(authRequest.getPassword(), BCrypt.gensalt()));

//        UserVO userVO = restTemplate.postForObject("http://user-service/users", authRequest, UserVO.class);
        
        UserVO userVO = new UserVO("1", "harsh@gmail.com", "harsh1", "USER");
        Assert.notNull(userVO, "Failed to register user. Please try again later");

        String accessToken = jwt.generate(userVO, "ACCESS");
        String refreshToken = jwt.generate(userVO, "REFRESH");

        return new AuthResponse(accessToken, refreshToken);

    }
}
