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

    @GetMapping("/children_reps_dashboard")
    public String childrenRepsDashboard() {
        return "children_reps_dashboard";
    }

    @GetMapping("/volunteers_dashboard")
    public String volunteersDashboard() {
        return "volunteers_dashboard";
    }

    @GetMapping("/doctors_dashboard")
    public String doctorsDashboard() {
        return "doctors_dashboard";
    }

    @GetMapping("/login_error")
    public String loginError() {
        return "login_error";
    }

    @GetMapping("/fragments/{page}")
    public String loadPage(@PathVariable String page) {
        return "fragments/" + page;
    }
}