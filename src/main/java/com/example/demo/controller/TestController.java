package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    @ResponseBody
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String Test(@RequestParam Integer id){
        return String.valueOf(id)+"_name";
    }

}
