package com.practice.universitysystem.controller;

import com.practice.universitysystem.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/curriculum")
public class CurriculumController {

    @Autowired
    CurriculumService curriculumService;

}
