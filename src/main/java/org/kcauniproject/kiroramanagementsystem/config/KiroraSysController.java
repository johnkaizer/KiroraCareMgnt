package org.kcauniproject.kiroramanagementsystem.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class KiroraSysController {

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/admin_dashboard")
    public String adminDashboard() {
        return "admin_dashboard";
    }

    @GetMapping("/fragments/{page}")
    public String loadPage(@PathVariable String page) {
        return "fragments/" + page;
    }
}
