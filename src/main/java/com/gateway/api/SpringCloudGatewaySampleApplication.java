package com.gateway.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringCloudGatewaySampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudGatewaySampleApplication.class, args);
	}

	// Simple Filter
	@Bean
	@Order(-1)
	public GlobalFilter simpleFilter() {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if (StringUtils.isEmpty(exchange.getRequest().getHeaders().getFirst("application-header")))
				request.mutate().header("application-header", "application-header").build();
			else
				request.mutate().header("application-header", "application-header-2").build();
			return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {
				exchange.getResponse().getHeaders().set("application-header", "application-header");
			}));
		};
	}
}