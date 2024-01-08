//package com.twodollar.tdboard.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
//
//@Configuration
//public class JPAConfig {
//    @Bean
//    public PageableHandlerMethodArgumentResolverCustomizer customize() {
//        return p -> {
//            p.setOneIndexedParameters(true);	// 1부터 시작
//            p.setMaxPageSize(10);				// size=10
//        };
//    }
//}
