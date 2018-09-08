package com.dw.exercise.controller;

import com.dw.exercise.entity.TestPaper;
import com.dw.exercise.security.AppAuthToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paper")
public class PaperController {
    @PostMapping("")
    public void createPaper(@RequestBody TestPaper paper){
        Integer userId = ((AppAuthToken) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        System.out.println(userId);
    }
}
