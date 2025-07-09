package org.kcauniproject.kiroramanagementsystem.usersmanagement;

import jakarta.annotation.PostConstruct;
import org.kcauniproject.kiroramanagementsystem.utils.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    @PostConstruct
    public void ensureAdminUserExists() {
        String adminUsername = "admin";
        String adminRole = "ADMIN";

        // Check if an admin user exists
        if (userRepository.findByRole(adminRole).isEmpty()) {
            User adminUser = new User();
            adminUser.setFullName("System Administrator");
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode("000000")); // Default password
            adminUser.setRole(adminRole);
            adminUser.setIdNumber("00000000");
            adminUser.setPhoneNumber("0000000000");
            adminUser.setCreated(LocalDateTime.now());
            adminUser.setUpdated(LocalDateTime.now());

            userRepository.save(adminUser);
            System.out.println("Default admin user created with username: " + adminUsername);
        } else {
            System.out.println("Admin user already exists. Skipping creation.");
        }
    }

    public List<User> getAllUsers() {
        // Return all users except admin users
        return userRepository.findByRoleNot("ADMIN");
    }

    public User addUser(User user, MultipartFile imageFile) {
        // Handle image upload if provided
        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = fileStorageService.storeEmployeeImage(imageFile);
        }

        // Set user specific fields
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImage(imagePath);
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());

        // Validate role
        if (!isValidRole(user.getRole())) {
            throw new RuntimeException("Invalid role: " + user.getRole());
        }

        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails, MultipartFile imageFile) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Handle image update if new image is provided
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (existingUser.getImage() != null) {
                fileStorageService.deleteEmployeeImage(existingUser.getImage());
            }
            String newImagePath = fileStorageService.storeEmployeeImage(imageFile);
            existingUser.setImage(newImagePath);
        }

        // Update other fields
        existingUser.setFullName(userDetails.getFullName());
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setIdNumber(userDetails.getIdNumber());
        existingUser.setPhoneNumber(userDetails.getPhoneNumber());
        existingUser.setUpdated(LocalDateTime.now());

        // Validate and update role if provided
        if (userDetails.getRole() != null && !userDetails.getRole().isEmpty()) {
            if (!isValidRole(userDetails.getRole())) {
                throw new RuntimeException("Invalid role: " + userDetails.getRole());
            }
            existingUser.setRole(userDetails.getRole());
        }

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Don't allow deletion of admin users
        if ("ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Cannot delete admin user");
        }

        // Delete associated image if exists
        if (user.getImage() != null) {
            fileStorageService.deleteEmployeeImage(user.getImage());
        }

        userRepository.delete(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public User authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        throw new RuntimeException("Invalid username or password");
    }

    public List<User> getUsersByRole(String role) {
        if (!isValidRole(role)) {
            throw new RuntimeException("Invalid role: " + role);
        }
        return userRepository.findByRole(role);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public boolean verifyResetCredentials(String username, String idNumber) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getIdNumber().equals(idNumber)) {
            throw new RuntimeException("Invalid credentials");
        }

        return true;
    }

    public void resetPassword(String username, String idNumber, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getIdNumber().equals(idNumber)) {
            throw new RuntimeException("Invalid credentials");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdated(LocalDateTime.now());
        userRepository.save(user);
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public long getUserCountByRole(String role) {
        if (!isValidRole(role)) {
            throw new RuntimeException("Invalid role: " + role);
        }
        return userRepository.countByRole(role);
    }

    private boolean isValidRole(String role) {
        return role != null && (
                "ADMIN".equals(role.toUpperCase()) ||
                        "CHILDREN_REPS".equals(role.toUpperCase()) ||
                        "VOLUNTEERS".equals(role.toUpperCase()) ||
                        "DOCTORS".equals(role.toUpperCase())
        );
    }
}