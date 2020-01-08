package edu.myrza.appdev.labone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/")
public class PagesController {

    @GetMapping
    public ModelAndView getHelloPage(){
        return new ModelAndView("index");
    }

}
