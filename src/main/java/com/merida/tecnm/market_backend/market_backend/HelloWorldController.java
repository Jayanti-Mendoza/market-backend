package com.merida.tecnm.market_backend.market_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/saludar")
public class HelloWorldController {
    @GetMapping("/saludo")
    public String helloWorld(){
        return "Hello World";
    }
}