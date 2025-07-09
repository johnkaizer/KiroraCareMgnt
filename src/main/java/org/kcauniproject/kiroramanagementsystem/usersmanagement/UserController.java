package org.kcauniproject.kiroramanagementsystem.usersmanagement;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> addUser(
            @RequestPart("user") User user,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        User createdUser = userService.addUser(user, imageFile);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestPart("user") User userDetails,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        User updatedUser = userService.updateUser(id, userDetails, imageFile);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user-image/{fileName}")
    public ResponseEntity<UrlResource> getUserImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get("./uploads/users/").resolve(fileName).normalize();
            UrlResource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.authenticate(username, password);
        session.setAttribute("loggedInUser", user); // Set the user in the session

        Map<String, String> response = new HashMap<>();
        String role = user.getRole();

        switch (role.toUpperCase()) {
            case "ADMIN":
                response.put("redirectUrl", "/admin_dashboard");
                break;
            case "CHILDREN_REPS":
                response.put("redirectUrl", "/children_reps_dashboard");
                break;
            case "VOLUNTEERS":
                response.put("redirectUrl", "/volunteers_dashboard");
                break;
            case "DOCTORS":
                response.put("redirectUrl", "/doctors_dashboard");
                break;
            default:
                response.put("redirectUrl", "/login_error");
        }

        return ResponseEntity.ok(response);
    }

    // Logout Endpoint
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        User user = (User) session.getAttribute("loggedInUser"); // Get the logged-in user

        if (user != null) {
            session.invalidate(); // Invalidate the session
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    // Get Logged-In User Details
    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUser() {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return ResponseEntity.ok(loggedInUser);
        }
        return ResponseEntity.status(401).body(null); // Unauthorized
    }

    // Get Logged-In User ID
    @GetMapping("/me/id")
    public ResponseEntity<Long> getLoggedInUserId() {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            return ResponseEntity.ok(loggedInUser.getId());
        }
        return ResponseEntity.status(401).body(null); // Unauthorized
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateLoggedInUser(
            @RequestPart("user") User userDetails,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User updatedUser = userService.updateUser(loggedInUser.getId(), userDetails, imageFile);
        session.setAttribute("loggedInUser", updatedUser); // Update session
        return ResponseEntity.ok(updatedUser);
    }

    // Get users by specific roles
    @GetMapping("/role/children-reps")
    public ResponseEntity<List<User>> getUsersWithChildrenRepsRole() {
        List<User> childrenReps = userService.getUsersByRole("CHILDREN_REPS");
        return ResponseEntity.ok(childrenReps);
    }

    @GetMapping("/role/volunteers")
    public ResponseEntity<List<User>> getUsersWithVolunteersRole() {
        List<User> volunteers = userService.getUsersByRole("VOLUNTEERS");
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/role/doctors")
    public ResponseEntity<List<User>> getUsersWithDoctorsRole() {
        List<User> doctors = userService.getUsersByRole("DOCTORS");
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/role/admin")
    public ResponseEntity<List<User>> getUsersWithAdminRole() {
        List<User> admins = userService.getUsersByRole("ADMIN");
        return ResponseEntity.ok(admins);
    }

    @PostMapping("/verify-reset")
    public ResponseEntity<?> verifyResetCredentials(
            @RequestParam String username,
            @RequestParam String idNumber) {
        try {
            boolean isValid = userService.verifyResetCredentials(username, idNumber);
            return ResponseEntity.ok().body(Map.of("valid", isValid));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String username,
            @RequestParam String idNumber,
            @RequestParam String newPassword) {
        try {
            userService.resetPassword(username, idNumber, newPassword);
            return ResponseEntity.ok().body(Map.of("message", "Password reset successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        return ResponseEntity.ok(userService.getUserCount());
    }

    @GetMapping("/count/role/{role}")
    public ResponseEntity<Long> getUserCountByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUserCountByRole(role));
    }
}