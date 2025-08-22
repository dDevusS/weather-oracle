package com.ddevuss.weather.oracle.aspect;

import com.ddevuss.weather.oracle.dto.UserCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuthentificationAspect {

    @Pointcut("within(com.ddevuss.weather.oracle.service.UserService)")
    public void isUserService() {
    }

    @Pointcut("within(com.ddevuss.weather.oracle.service.SecurityService)")
    public void isSecurityService() {
    }

    @After("isUserService() " +
           "&& execution(public * save(*)) " +
           "&& args(userCreateDto)")
    public void logSavingNewUser(UserCreateDto userCreateDto) {
        log.info("New user with login {} has been saved", userCreateDto.getLogin());
    }

    @After("isUserService() " +
           "&& execution(public * loadUserByUsername(*)) " +
           "&& args(login)")
    public void logSingingUser(String login) {
        log.info("User with login {} has singed in", login);
    }

    @AfterThrowing(value = "isSecurityService()", throwing = "exception")
    public void logAccessDeniedForLocationDeleting(AccessDeniedException exception) {
        log.warn(exception.getMessage());
    }
}
