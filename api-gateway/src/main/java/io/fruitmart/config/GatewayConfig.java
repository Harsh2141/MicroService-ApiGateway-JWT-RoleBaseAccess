package io.fruitmart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Autowired
	AuthenticationFilter filter;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("order-service",
						r -> r.path("/order-service/api/order/**", "/api/order/**").filters(f -> f.filter(filter))
								.uri("lb://order-service"))

				.route("auth-service", r -> r.path("/auth-service/auth/**", "/api/auth/**")
						.filters(f -> f.filter(filter)).uri("lb://auth-service"))
				.build();
	}

}
