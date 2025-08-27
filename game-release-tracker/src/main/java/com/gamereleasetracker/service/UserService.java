package com.gamereleasetracker.service;

import com.gamereleasetracker.model.Role;
import com.gamereleasetracker.model.User;
import com.gamereleasetracker.repository.RoleRepository;
import com.gamereleasetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.gamereleasetracker.dto.UserDto;
import com.gamereleasetracker.dto.UserCreateRequestDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The user with the specified username.
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user to retrieve.
     * @return The user with the specified email.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return An Optional containing the user if found, or empty if not found.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Checks if a user exists by their username.
     *
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if a user exists by their email.
     *
     * @param email The email to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Deletes a user.
     * This method should be used with caution as it will remove the user from the database.
     * Should be executable only by admins or authorized users
     *
     * @param userDto The UserDto representing the user to delete.
     */
    public void deleteUser(UserDto userDto) {
        User user = userRepository.findById(userDto.id())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.id()));
        userRepository.delete(user);
    }

    /**
     * Creates a new user.
     * This method checks for existing usernames and emails to avoid duplicates.
     * Should be executable only by admins or authorized users
     *
     * @param userDto The UserCreateRequestDto containing user details.
     * @return The created user.
     */
    @Transactional
    public User createUser(UserCreateRequestDto userDto) {
        if (userRepository.existsByUsername(userDto.username())) {
            throw new RuntimeException("Username already exists: " + userDto.username());
        }
        if (userRepository.existsByEmail(userDto.email())) {
            throw new RuntimeException("Email already exists: " + userDto.email());
        }

        User user = new User();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPasswordHash(passwordEncoder.encode(userDto.password())); // Assume password is hashed before saving
        user.setCreatedAt(Instant.now());
        user.setEnableNotifications(true);
        Role role = new Role();
        role.setName("ROLE_USER"); // Default role
        user.setRole(roleRepository.save(role));

        return userRepository.save(user);
    }

    // /**
    //  * Updates a user's email.
    //  * This method checks for existing emails to avoid duplicates.
    //  * Should be executable only by admins or authorized users
    //  *
    //  * @param userDto The userDto representing the user whose email is to be updated.
    //  * @param newEmail The new email.
    //  * @return The updated user.
    //  */
    // @Transactional
    // public User updateUserEmail(UserDto userDto, String newEmail) {
    //     if (userRepository.existsByEmail(newEmail)) {
    //         throw new RuntimeException("Email already exists: " + newEmail);
    //     }
    //     User user = userRepository.findById(userDto.id())
    //             .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.id()));
    //     user.setEmail(newEmail);
    //     return userRepository.save(user);
    // }


    // /**
    //  * Updates a user's username.
    //  * This method checks for existing usernames to avoid duplicates.
    //  * Should be executable only by admins or authorized users
    //  *
    //  * @param userDto The userDto representing the user whose username is to be updated.
    //  * @param newUsername The new username.
    //  * @return The updated user.
    //  */
    // @Transactional
    // public User updateUserUsername(UserDto userDto, String newUsername) {
    //     if (userRepository.existsByUsername(newUsername)) {
    //         throw new RuntimeException("Username already exists: " + newUsername);
    //     }
    //     User user = userRepository.findById(userDto.id())
    //             .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.id()));
    //     user.setUsername(newUsername);
    //     return userRepository.save(user);
    // }

    /**
     * Changes a user's password.
     * This method should be used with caution as it will modify the user's credentials.
     * Should be executable only by admins or authorized users
     *
     * @param userDto The userDto representing the user whose password is to be changed.
     * @param newPassword The new password.
     */
    @Transactional
    public void changeUserPassword(UserDto userDto, String newPassword) {
        User user = userRepository.findById(userDto.id())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.id()));
        user.setPasswordHash(passwordEncoder.encode(newPassword)); // Assume password is hashed before saving
        userRepository.save(user);
    }

    @Transactional
    public User updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.id())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.id()));

        if (userDto.email() != null && !userDto.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDto.email())) {
                throw new RuntimeException("Email already exists: " + userDto.email());
            }
            user.setEmail(userDto.email());
        }
        if (userDto.username() != null && !userDto.username().equals(user.getUsername())) {
            if (userRepository.existsByUsername(userDto.username())) {
                throw new RuntimeException("Username already exists: " + userDto.username());
            }
            user.setUsername(userDto.username());
        }
        if (userDto.enableNotifications() != user.isEnableNotifications()) {
            user.setEnableNotifications(userDto.enableNotifications());
        }

        return userRepository.save(user);
    }

    /**
     * Retrieves the UserRepository instance.
     * This method is typically used for testing purposes.
     *
     * @return The UserRepository instance.
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }
}
