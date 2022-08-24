package com.practice.universitysystem;

import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.curriculum.CurriculumService;
import com.practice.universitysystem.service.grade.GradeService;
import com.practice.universitysystem.service.subject.SubjectService;
import com.practice.universitysystem.service.users.student.StudentService;
import com.practice.universitysystem.service.users.teacher.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
class UniversitysystemApplicationTests {

	@Autowired
	CurriculumService curriculumService;

	@Autowired
	GradeService gradeService;

	@Autowired
	SubjectService subjectService;

	@Autowired
	StudentService studentService;

	@Autowired
	TeacherService teacherService;

	@Autowired
	AuthService authService;

	@Test
	void contextLoads() {
		assertThat(curriculumService, notNullValue());
		assertThat(gradeService, notNullValue());
		assertThat(subjectService, notNullValue());
		assertThat(studentService, notNullValue());
		assertThat(teacherService, notNullValue());
		assertThat(authService, notNullValue());
	}

}
