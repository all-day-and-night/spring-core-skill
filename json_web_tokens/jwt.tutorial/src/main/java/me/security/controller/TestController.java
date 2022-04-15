package me.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class TestController {

    @GetMapping
    public ResponseEntity<String> control(){
        return ResponseEntity.ok("normal");
    }
}
