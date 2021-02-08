package com.javatechie.report.service;

import com.javatechie.report.entity.Employee;
import com.javatechie.report.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private EmployeeRepository repository;

    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        String path = "C:\\Users\\basan\\Desktop\\Report";
        List<Employee> employees = repository.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:employees.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\employees.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\employees.pdf");
        }

        return "report generated in path : " + path;
    }

    public String export(String format) throws Exception {

        final String DESTINATION_PATH = "/home/viveksoni100/Downloads/";

        List<Employee> employees = getAllEmployees();
        JasperReport jasperReport = loadJRXMLFileAndCompileIt();
        JRBeanCollectionDataSource dataSource = mapEmployeeDataToReport(employees);
        Map<String, Object> parameters = fillTheParameters();
        JasperPrint jasperPrint = fillReport(jasperReport, parameters, dataSource);

        exportTheReport(jasperPrint, DESTINATION_PATH, format);

        return "Export is done at : " + DESTINATION_PATH;

    }

    private void exportTheReport(JasperPrint jasperPrint, String destination_path, String format) throws JRException {

        final String FILE_NAME = "Employees";

        switch (format) {
            case "html":
                JasperExportManager.exportReportToHtmlFile(jasperPrint, destination_path + FILE_NAME + ".html");
                break;
            case "pdf":
                JasperExportManager.exportReportToPdfFile(jasperPrint, destination_path + FILE_NAME + ".pdf");
        }

    }

    private Map<String, Object> fillTheParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Vivek Soni");
        return parameters;
    }

    private JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) throws JRException {
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return jasperPrint;
    }

    private JRBeanCollectionDataSource mapEmployeeDataToReport(List<Employee> employees) {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        return dataSource;
    }

    private JasperReport loadJRXMLFileAndCompileIt() throws Exception {
        JasperReport jasperReport = null;
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:jrxmls/emp_execute.jrxml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (file == null) {
            throw new Exception("Null file can not be compiled");
        } else {
            jasperReport = compileFile(file);
        }
        return jasperReport;
    }

    private JasperReport compileFile(File file) {
        JasperReport jasperReport = null;
        try {
            jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        } catch (JRException e) {
            e.printStackTrace();
        }
        return jasperReport;
    }

    private List<Employee> getAllEmployees() {
        return repository.findAll();
    }
}
