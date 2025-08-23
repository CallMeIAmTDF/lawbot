package com.example.template.controller;

import com.example.template.service.SensitiveWordService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {
    SensitiveWordService sensitiveWordService;

    @GetMapping("/check")
    public Boolean hello(@RequestParam(name = "text") String text){
        return sensitiveWordService.contains(text);
    }

    @GetMapping("")
    public String test(){
        return "Hello World";
    }
}
