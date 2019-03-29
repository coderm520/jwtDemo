package com.example.demo.controller;

import com.example.demo.components.Login;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @Login
    public String GetStudentNameById(@RequestParam Integer id){
        return String.valueOf(id)+"_name";
    }
}
