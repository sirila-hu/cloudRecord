package com.example.top_sirilahu;

import Listener.SirilahuHttpSessionEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.ServletContext;

@SpringBootApplication
@ServletComponentScan
public class Application {
    @Autowired
    ServletContext servletContext;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
