package com.tpqms2061.dashboard.controller;


import com.tpqms2061.dashboard.model.CustomerRank;
import com.tpqms2061.dashboard.model.DailySales;
import com.tpqms2061.dashboard.repository.DashboardRepository;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final DashboardRepository dashboardRepository;

    public DashboardController(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @GetMapping("/")
    public String dailySales(Model model) {
        List<DailySales> sales = dashboardRepository.findDailySales();
        model.addAttribute("sales", sales);

        return "daily-sales";
    }

    @GetMapping("/customer-ranking")
    public String customerRanking(Model model) {
        List<CustomerRank> ranks = dashboardRepository.findCustomerRankings();
        model.addAttribute("ranks", ranks);

        return "customer-ranking";
    }
}
