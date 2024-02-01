//package com.twodollar.tdboard;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.support.ErrorPageFilter;
//import org.springframework.context.annotation.Bean;
//
//public class ErrorPageRegistry {
//
//    @Bean
//    public ErrorPageFilter errorPageFilter(){
//        return new ErrorPageFilter();
//    }
//
//    @Bean
//    public FilterRegistrationBean DisabledErrorPageFilter(ErrorPageFilter filter){
//        FilterRegistrationBean filterRegistration = new FilterRegistrationBean<>();
//        filterRegistration.setFilter(filter);
//        filterRegistration.setName("disabledErrorPageFilter");
//        filterRegistration.setEnabled(false);
//        return filterRegistration;
//    }
//
//}
