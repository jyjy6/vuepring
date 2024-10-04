package com.spvue;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/testing")
    public String test(){
        return "redirect:/hi.html";
    }
}
