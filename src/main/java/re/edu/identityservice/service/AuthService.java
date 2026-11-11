package re.edu.identityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.edu.identityservice.dto.RegisterRequest;
import re.edu.identityservice.dto.UserResponse;
import re.edu.identityservice.entity.User;
import re.edu.identityservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Xử lý đăng ký tài khoản
    @Transactional
    public UserResponse register(RegisterRequest request) {

        // 1. Kiểm tra dữ liệu đầu vào
        if (request.username() == null ||
                request.username().isBlank() ||
                request.password() == null ||
                request.password().isBlank()) {

            throw new IllegalArgumentException(
                    "Username and password are required"
            );
        }
        // 2. Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }
        // 3. Mã hóa password
        String encodedPassword = passwordEncoder.encode(request.password());

        // 4. Tạo User mới
        User user = new User(
                null,
                request.username(),
                encodedPassword,
                "USER"
        );

        // 5. Lưu User vào Database
        User savedUser = userRepository.save(user);

        // 6. Trả dữ liệu an toàn về Client
        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }
}
