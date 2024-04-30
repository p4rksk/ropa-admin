package com.example.finalproject.domain.order;

import com.example.finalproject.domain.admin.Admin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final HttpServletRequest request;
    private final HttpSession session;
    private final OrderService orderService;


    // 주문 상세 페이지
    @GetMapping("/api/order-detail")
    public String orderDetail() {
        return "order-history";
    }
}
