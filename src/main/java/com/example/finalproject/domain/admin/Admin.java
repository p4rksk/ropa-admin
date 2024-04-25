package com.example.finalproject.domain.admin;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Data
@Table(name = "admin_tb")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String email; //아이디

    @Column(nullable = false)
    private String password; //비밀번호

    @Column(nullable = false)
    private String brandName; //브랜드명

    @Column(nullable = false)
    private AdminRole role; // 관리자 / 브랜드

    @Column(nullable = false)
    private String address; //주소

    @Column(nullable = false)
    private String businessNum; // 사업자 번호 (관리자, 브랜드)

    @Column(nullable = false)
    private LocalDateTime createdAt; // 브랜드, 관리자 회원가입 시간

    private LocalDateTime updateAt; // 관리자 / 브랜드 수정 날짜

    @Builder
    public Admin(Integer id, String email, String password, String brandName, AdminRole role, String address, String businessNum, LocalDateTime updateAt, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.brandName = brandName;
        this.role = role;
        this.address = address;
        this.businessNum = businessNum;
        this.updateAt = updateAt;
        this.createdAt = createdAt;
    }


    enum AdminRole {
        ADMIN, BRAND
    }
}
