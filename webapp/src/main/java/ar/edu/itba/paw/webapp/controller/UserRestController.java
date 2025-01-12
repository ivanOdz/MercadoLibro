package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.services.UserReviewService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.form.MailForm;
import ar.edu.itba.paw.webapp.form.PasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private PawUserDetailsService userDetailsService;

    @Autowired
    private UserReviewService userReviewService;


    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a test endpoint");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(Authentication authentication, @RequestHeader("Authorization") String authHeader) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrorResponse("Authentication failed"));
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse("Missing or invalid Authorization header"));
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        Map<String, Object> response = new HashMap<>();
        response.put("username", authentication.getName());
        response.put("roles", authentication.getAuthorities());
        response.put("token", token);
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserForm userForm) {
        if (userService.userExists(userForm.getMail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(buildErrorResponse("User already exists"));
        }

        User user = userService.createUser(userForm.getUsername(), userForm.getMail(), userForm.getPassword(), Locale.getDefault().toLanguageTag());
        Map<String, Object> response = new HashMap<>();
       // response.put("user", UserDto.fromUser(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable long userId) {
       // User user = userService.getUserById(userId);
        Map<String, Object> response = new HashMap<>();
       // response.put("user", UserDto.fromUser(user));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{userId}/change_username")
    public ResponseEntity<Map<String, Object>> changeUsername(@PathVariable long userId, @RequestParam String newUsername) {
        boolean updated = userService.changeUserName(userId, newUsername);
        if (updated) {
            return ResponseEntity.ok(buildSuccessResponse("Username updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse("Failed to update username"));
    }

    @PostMapping("/{userId}/locations")
    public ResponseEntity<Map<String, Object>> addLocation(@PathVariable Long userId, @RequestParam String locationString) {
        userService.addLocation(userId, locationString);
        return ResponseEntity.ok(buildSuccessResponse("Location added successfully"));
    }

    @DeleteMapping("/{userId}/locations/{locationId}")
    public ResponseEntity<Map<String, Object>> removeLocation(@PathVariable Long userId, @PathVariable Long locationId) {
        userService.removeLocation(userId, locationId);
        return ResponseEntity.ok(buildSuccessResponse("Location removed successfully"));
    }

    @PostMapping("/language")
    public ResponseEntity<Map<String, Object>> changeLanguage(@RequestParam(name = "lang") String lang, @RequestParam(name = "userId") long userId) {
        Locale locale = Locale.forLanguageTag(lang);
        //userService.setUserLanguage(userService.getUserById(userId), lang);
        return ResponseEntity.ok(buildSuccessResponse("Language updated successfully"));
    }

    // Utility methods to build standardized responses
    private Map<String, Object> buildSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        return response;
    }

    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }
}
