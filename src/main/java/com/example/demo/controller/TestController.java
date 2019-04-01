package com.example.demo.controller;

import com.example.demo.components.Login;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @Login
    public ModelAndView Index(String token){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("index");
        mv.addObject("key","indexaaa");
        return mv;
    }


    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @Login
    public String GetStudentNameById(@RequestParam Integer id){
        return String.valueOf(id)+"_name";
    }

    @ResponseBody
    @RequestMapping(value = "/addrole",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @Login
    public Integer AddRole(@RequestParam String name){
        return 100;
    }
}
