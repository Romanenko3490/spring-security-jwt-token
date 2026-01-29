package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper mapper;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto save(User user) {
        return mapper.toUserDto(userRepository.save(user));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }


}
