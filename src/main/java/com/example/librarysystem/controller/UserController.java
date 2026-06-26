package com.example.librarysystem.controller;

import com.example.librarysystem.dto.*;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 获取当前登录用户信息
    @GetMapping("/current")
    public ResponseEntity<UserProfileDto> getCurrentUser(@AuthenticationPrincipal User user) {
        UserProfileDto profile = userService.getUserProfile(user.getId());
        return ResponseEntity.ok(profile);
    }

    // 更新用户个人信息
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileDto updateDto
    ) {
        userService.updateUserProfile(user.getId(), updateDto);
        return ResponseEntity.ok(new ApiResponse(true, "个人资料更新成功"));
    }

    // 修改密码
    @PutMapping("/password")
    public ResponseEntity<ApiResponse> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordDto passwordDto
    ) {
        userService.changePassword(user.getId(), passwordDto);
        return ResponseEntity.ok(new ApiResponse(true, "密码修改成功"));
    }

    // 获取用户借阅统计
    @GetMapping("/borrow-stats")
    public ResponseEntity<BorrowStatsDto> getBorrowStats(@AuthenticationPrincipal User user) {
        BorrowStatsDto stats = userService.getBorrowStats(user.getId());
        return ResponseEntity.ok(stats);
    }

    // 获取用户借阅历史
    @GetMapping("/borrow-history")
    public ResponseEntity<List<BorrowHistoryDto>> getBorrowHistory(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search
    ) {
        List<BorrowHistoryDto> history = userService.getBorrowHistory(user.getId(), status, search);
        return ResponseEntity.ok(history);
    }

    // 删除用户账户
    @DeleteMapping("/account")
    public ResponseEntity<ApiResponse> deleteAccount(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody DeleteAccountDto deleteDto
    ) {
        userService.deleteUserAccount(user.getId(), deleteDto.getPassword());
        return ResponseEntity.ok(new ApiResponse(true, "账户已成功删除"));
    }
}
