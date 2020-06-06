package com.example.demo.service;

import com.example.demo.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    public List<Student> getStudents(){
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "Farrukh"));
        studentList.add(new Student(2, "Farhan"));
        studentList.add(new Student(3, "Naveen"));
        return studentList;
    }
}
