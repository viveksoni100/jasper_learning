package com.javatechie.report.service;

import com.javatechie.report.dto.Subject;
import com.javatechie.report.entity.Employee;
import com.javatechie.report.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBaseTextField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.awt.*;
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

    public String subReport(String format) throws Exception {

        final String DESTINATION_PATH = "/home/viveksoni100/Downloads/";
        final String JRXML_FILE_NAME = "subReportLearning";
        List<Subject> subjects = getSubjectInformation();
        final String FILE_NAME = "SubReport";
        final String SUB_JRXML_FILE_NAME = "emp_execute";
        List<Employee> employees = getAllEmployees();

        JasperReport compiledSubReport = loadJRXMLFileAndCompileIt(SUB_JRXML_FILE_NAME);
        JRBeanCollectionDataSource subDataSource = mapEmployeeDataToReport(employees);

        JasperReport jasperReport = loadJRXMLFileAndCompileIt(JRXML_FILE_NAME);
        JRBeanCollectionDataSource dataSource = mapSubjectReportToReport(subjects);
        JRBeanCollectionDataSource chartDataSource = dataSource;
        Map<String, Object> parameters = getTheParamersToBePassedForSubReort(dataSource, compiledSubReport, subDataSource);
        fillTheParameters(parameters);
        JasperPrint jasperPrint = fillReport(jasperReport, parameters, chartDataSource);
        exportTheReport(jasperPrint, DESTINATION_PATH, format, FILE_NAME);

        return "Export is done at : " + DESTINATION_PATH;
    }

    private Map<String, Object> getTheParamersToBePassedForSubReort(JRBeanCollectionDataSource dataSource, JasperReport compiledSubReport, JRBeanCollectionDataSource subDataSource) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("studentName", "Vivek Soni");
        parameters.put("tableData", dataSource);
        parameters.put("subReport", compiledSubReport);
        parameters.put("subDataSource", subDataSource);
        return parameters;
    }

    public String exportCustomReport(String format) throws Exception {

        final String DESTINATION_PATH = "/home/viveksoni100/Downloads/";
        final String JRXML_FILE_NAME = "CustomReportLearning";
        List<Subject> subjects = getSubjectInformation();
        final String FILE_NAME = "CustomReport";

        JasperReport jasperReport = loadJRXMLFileAndCompileIt(JRXML_FILE_NAME);
        JRBeanCollectionDataSource dataSource = mapSubjectReportToReport(subjects);
        JRBeanCollectionDataSource chartDataSource = dataSource;
        Map<String, Object> parameters = getTheParamersToBePassedForSubject(dataSource);
        fillTheParameters(parameters);
        JasperPrint jasperPrint = fillReport(jasperReport, parameters, chartDataSource);
        exportTheReport(jasperPrint, DESTINATION_PATH, format, FILE_NAME);

        return "Export is done at : " + DESTINATION_PATH;
    }

    private Map<String, Object> getTheParamersToBePassedForSubject(JRBeanCollectionDataSource dataSource) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("studentName", "Vivek Soni");
        parameters.put("tableData", dataSource);
        return parameters;
    }

    private JRBeanCollectionDataSource mapSubjectReportToReport(List<Subject> subjects) {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(subjects);

        return dataSource;
    }

    private List<Subject> getSubjectInformation() {

        List<Subject> subjectList = new ArrayList<>();

        subjectList.add(new Subject("Java", 80));
        subjectList.add(new Subject("Mongo", 100));
        subjectList.add(new Subject("PHP", 80));

        return subjectList;
    }

    public String export(String format) throws Exception {

        final String DESTINATION_PATH = "/home/viveksoni100/Downloads/";
        final String JRXML_FILE_NAME = "emp_execute";
        Map<String, Object> parameters = getTheParamersToBePassedForEmp();
        final String FILE_NAME = "Employees";

        List<Employee> employees = getAllEmployees();
        JasperReport jasperReport = loadJRXMLFileAndCompileIt(JRXML_FILE_NAME);
        changeTheColorOfParaNameOfDonRunTime(jasperReport); //  optional
        JRBeanCollectionDataSource dataSource = mapEmployeeDataToReport(employees);
        fillTheParameters(parameters);
        JasperPrint jasperPrint = fillReport(jasperReport, parameters, dataSource);

        exportTheReport(jasperPrint, DESTINATION_PATH, format, FILE_NAME);

        return "Export is done at : " + DESTINATION_PATH;

    }

    private Map<String, Object> getTheParamersToBePassedForEmp() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nameOfDon", "Vivek Soni");
        return parameters;
    }

    private void changeTheColorOfParaNameOfDonRunTime(JasperReport jasperReport) {
        JRBaseTextField textField = (JRBaseTextField) jasperReport.getTitle().getElementByKey("nameOfDon_key");
        textField.setForecolor(Color.YELLOW);
    }

    private void exportTheReport(JasperPrint jasperPrint, String destination_path, String format, String FILE_NAME) throws JRException {

        switch (format) {
            case "html":
                JasperExportManager.exportReportToHtmlFile(jasperPrint, destination_path + FILE_NAME + ".html");
                break;
            case "pdf":
                JasperExportManager.exportReportToPdfFile(jasperPrint, destination_path + FILE_NAME + ".pdf");
        }

    }

    private Map<String, Object> fillTheParameters(Map<String, Object> parameters) {
        parameters.put("nameOfDon", "Vivek Soni");
        return parameters;
    }

    private JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) throws JRException {
        if (dataSource != null) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            return jasperPrint;
        } else {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            return jasperPrint;
        }

    }

    private JRBeanCollectionDataSource mapEmployeeDataToReport(List<Employee> employees) {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

        return dataSource;
    }

    private JasperReport loadJRXMLFileAndCompileIt(String JRXML_FILE_NAME) throws Exception {
        JasperReport jasperReport = null;
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:jrxmls/" + JRXML_FILE_NAME + ".jrxml");
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
