package com.Financial_Management_System.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Financial_Management_System.DTO.ReportDTO;
import com.Financial_Management_System.Entity.ReportEntity;
import com.Financial_Management_System.Service.ReportService;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/{year}")
    public ReportEntity generateYearlyReport(@PathVariable Integer year) throws Exception {
        return reportService.generateYearlyReport(year);
    }

    @GetMapping("/custom")
    public ReportEntity generateCustomReport(@RequestBody ReportDTO reportDTO) throws Exception {
        return reportService.generateCustomReport(reportDTO.getFromDate(), reportDTO.getUptoDate());
    }
}
