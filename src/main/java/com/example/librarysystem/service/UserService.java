package com.example.librarysystem.service;

import com.example.librarysystem.dto.*;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.exception.InvalidPasswordException;
import com.example.librarysystem.exception.UserAlreadyExistsException;
import com.example.librarysystem.exception.UserNotFoundException;
import com.example.librarysystem.repository.BorrowRecordRepository;
import com.example.librarysystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BorrowRecordRepository borrowRecordRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(String username, String password, String email,
                            String firstName, String lastName) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("该用户名已被注册");
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("该邮箱已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole("USER");
        user.setActive(true);

        return userRepository.save(user);
    }

    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("用户不存在"));

        return new UserProfileDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    @Transactional
    public void updateUserProfile(Long userId, UpdateProfileDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("用户不存在"));

        // 检查用户名是否已被使用（排除当前用户）
        if (!user.getUsername().equals(updateDto.getUsername())) {
            if (userRepository.existsByUsername(updateDto.getUsername())) {
                throw new IllegalArgumentException("该用户名已被使用");
            }
        }

        // 检查邮箱是否已被使用（排除当前用户）
        if (!user.getEmail().equals(updateDto.getEmail())) {
            if (userRepository.existsByEmail(updateDto.getEmail())) {
                throw new IllegalArgumentException("该邮箱已被注册");
            }
        }

        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());
        user.setEmail(updateDto.getEmail());
        user.setUsername(updateDto.getUsername());
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordDto passwordDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("用户不存在"));

        // 验证当前密码
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("当前密码不正确");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

    public BorrowStatsDto getBorrowStats(Long userId) {
        long totalBorrowed = borrowRecordRepository.countByUserId(userId);
        long returned = borrowRecordRepository.countByUserIdAndStatus(userId, "RETURNED");
        long borrowing = borrowRecordRepository.countByUserIdAndStatus(userId, "BORROWED");
        long overdue = borrowRecordRepository.countByUserIdAndStatusAndDueDateBefore(
                userId, "BORROWED", LocalDate.now());

        return new BorrowStatsDto(totalBorrowed, returned, borrowing, overdue);
    }

    public List<BorrowHistoryDto> getBorrowHistory(Long userId, String status, String search) {
        List<Object[]> historyRecords;

        if (status != null && !status.equals("all")) {
            historyRecords = borrowRecordRepository.findHistoryByUserIdAndStatus(userId, status);
        } else if (search != null && !search.isEmpty()) {
            historyRecords = borrowRecordRepository.findHistoryByUserIdAndBookTitle(userId, search);
        } else {
            historyRecords = borrowRecordRepository.findHistoryByUserId(userId);
        }

        return historyRecords.stream()
                .map(record -> new BorrowHistoryDto(
                        (Long) record[0],       // borrowId
                        (Long) record[1],       // bookId
                        (String) record[2],     // bookTitle
                        (String) record[3],     // bookAuthor
                        ((java.sql.Date) record[4]).toLocalDate(), // borrowDate
                        record[5] != null ? ((java.sql.Date) record[5]).toLocalDate() : null, // returnDate
                        ((java.sql.Date) record[6]).toLocalDate(), // dueDate
                        (String) record[7]      // status
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserAccount(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("用户不存在"));

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("密码不正确");
        }

        // 先清空关联，利用 JPA 级联删除借阅记录
        user.getBorrowRecords().clear();
        userRepository.delete(user);
    }
}
