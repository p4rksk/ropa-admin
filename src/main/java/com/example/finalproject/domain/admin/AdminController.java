package com.example.finalproject.domain.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class AdminController {
    private final AdminService adminService;
    private final HttpSession session;

    // 회원가입
    @PostMapping("/join")
    public String join(AdminRequest.JoinDTO reqDTO, HttpServletRequest req) {
        Admin admin = adminService.join(reqDTO);
        req.setAttribute("Admin", admin);
        return "/admin/join-form";
    }

    // 회원가입 폼
    @GetMapping("/joinForm")
    public String joinForm() {
        return "/admin/join-form";
    }

    // 관리자 매출관리 페이지
    @GetMapping("/api/admin-sales-manage")
    public String adminSalesManage() {
        return "sales/admin-sales-manage";
    }

    // 브랜드 매출관리 페이지
    @GetMapping("/api/brand-sales-manage")
    public String brandSalesManage() {
        return "sales/admin-sales-manage";
    }

    // 회원 관리 페이지
    @GetMapping("/api/user-manage")
    public String userManage() {
        return "admin/user-manage";
    }

}
