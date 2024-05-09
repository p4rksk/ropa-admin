package com.example.finalproject.domain.order;

import com.example.finalproject._core.utils.ApiUtil;
import com.example.finalproject.domain.user.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderRestController {
    private final OrderService orderService;
    private final HttpSession session;

    // 총 주문 페이지 + 배송지 설정 화면
    @GetMapping("/app/order-page")
    public ResponseEntity<?> orderPage(){
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        OrderResponse.PageView respDTO = orderService.orderPage(sessionUser.getId());
        return ResponseEntity.ok(new ApiUtil<>(respDTO));
    }

    // 주문하기 + 배송지 정보까지 세이브
    @PostMapping("/app/order")
    public ResponseEntity<?> OrderSave(@RequestBody OrderRequest.SaveOrder reqDTO) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
        OrderResponse.SaveOrder respDTO = orderService.saveOrder(reqDTO, sessionUser.getId());
        return ResponseEntity.ok(new ApiUtil<>(respDTO));
    }
}
