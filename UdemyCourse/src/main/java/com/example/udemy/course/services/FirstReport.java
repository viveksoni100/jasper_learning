package com.example.udemy.course.services;

import com.example.udemy.course.dto.Student;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirstReport {

    public void executeFirstReport() {

        try {
            String filePath = "/home/viveksoni100/GIT_REPO/jasper_learning/UdemyCourse/src/main/resources/FirstReport.jrxml";
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("studentName", "Voldemort");

            Student student1 = new Student(1L, "Vivek", "Soni", "Amchi Mumbai", "Bombay");
            Student student2 = new Student(1L, "Khusita", "Soni", "Vibrant Street", "Pune");

            List<Student> studentList = new ArrayList<>();
            studentList.add(student1);
            studentList.add(student2);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentList);

            JasperReport report = JasperCompileManager.compileReport(filePath);

            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

            JasperExportManager.exportReportToPdfFile(print, "/home/viveksoni100/GIT_REPO/jasper_learning/UdemyCourse/src/jasper/FirstReport.pdf");

            System.out.println("done done londone!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
