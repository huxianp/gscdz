package com.gscdz.listener;

import com.gscdz.model.CustomUser;
import com.gscdz.repository.CustomUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
@WebListener
@Slf4j
public class MyHttpSessionListener implements HttpSessionListener {
    @Autowired
    private CustomUserRepository customUserRepo;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("Session Start");
        WebApplicationContextUtils
           .getRequiredWebApplicationContext(se.getSession().getServletContext())
           .getAutowireCapableBeanFactory()
           .autowireBean(this);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.info("Session destroyed");
        HttpSession session=se.getSession();
        CustomUser customUser=(CustomUser) session.getAttribute("userInf");
        customUserRepo.save(customUser);
    }

}
