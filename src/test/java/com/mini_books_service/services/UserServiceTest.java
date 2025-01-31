package com.mini_books_service.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mini_books_service.models.dtos.UserDTO;
import com.mini_books_service.models.entities.User;
import com.mini_books_service.models.viewmodels.UserViewModel;
import com.mini_books_service.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  // Test for createNewUser
  @Test
  public void testCreateNewUser() {
    // Arrange: create a view model for a new user
    UserViewModel userViewModel = new UserViewModel();
    userViewModel.setName("testname");
    userViewModel.setEmail("email@test.org");
    userViewModel.setPassword("testpass1");

    // Arrange: stub password encoder to return an encoded password
    String encodedPassword = "encodedPass";
    when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

    // Arrange: stub repository save to return a user entity with the same fields
    User savedUser = new User();
    savedUser.setName(userViewModel.getName());
    savedUser.setEmail(userViewModel.getEmail());
    savedUser.setPasswordHash(encodedPassword);
    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    // Act: call the service method
    UserDTO result = userService.createNewUser(userViewModel);

    // Assert: verify the interactions and that a non-null DTO is returned
    verify(passwordEncoder, times(1)).encode("testpass1");
    verify(userRepository, times(1)).save(any(User.class));
    assertNotNull(result);
  }

  // Test for login with a successful match
  @Test
  public void testLoginSuccess() {
    // Arrange
    UserViewModel userViewModel = new UserViewModel();
    userViewModel.setEmail("email@test.org");
    userViewModel.setPassword("testpass1");

    User user = new User();
    user.setEmail("email@test.org");
    user.setPasswordHash("encodedPass");

    when(userRepository.findByEmail("email@test.org")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("testpass1", "encodedPass")).thenReturn(true);

    // Act
    UserDTO result = userService.login(userViewModel);

    // Assert
    verify(userRepository, times(1)).findByEmail("email@test.org");
    verify(passwordEncoder, times(1)).matches("testpass1", "encodedPass");
    assertNotNull(result);
  }

  // Test for login failure due to wrong password
  @Test
  public void testLoginFailureDueToWrongPassword() {
    // Arrange
    UserViewModel userViewModel = new UserViewModel();
    userViewModel.setEmail("email@test.org");
    userViewModel.setPassword("wrongpass");

    User user = new User();
    user.setEmail("email@test.org");
    user.setPasswordHash("encodedPass");

    when(userRepository.findByEmail("email@test.org")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrongpass", "encodedPass")).thenReturn(false);

    // Act
    UserDTO result = userService.login(userViewModel);

    // Assert
    verify(userRepository, times(1)).findByEmail("email@test.org");
    verify(passwordEncoder, times(1)).matches("wrongpass", "encodedPass");
    assertNull(result);
  }

  // Test for login failure when user is not found
  @Test
  public void testLoginFailureUserNotFound() {
    // Arrange
    UserViewModel userViewModel = new UserViewModel();
    userViewModel.setEmail("email@test.org");
    userViewModel.setPassword("testpass1");

    when(userRepository.findByEmail("email@test.org")).thenReturn(Optional.empty());

    // Act
    UserDTO result = userService.login(userViewModel);

    // Assert
    verify(userRepository, times(1)).findByEmail("email@test.org");
    assertNull(result);
  }

  // Test for updateUser when the existing password matches
  @Test
  public void testUpdateUserPasswordMatches() {
    // Arrange: Create a view model with an idHash for user id 1
    UserViewModel userViewModel = new UserViewModel();
    userViewModel.setName("updatedName");
    userViewModel.setEmail("updated@test.org");
    userViewModel.setPassword("testpass1");
    String idHash = Base64.getEncoder().encodeToString("1".getBytes());
    userViewModel.setIdHash(idHash);

    // Arrange: Existing user with the same password hash
    User existingUser = new User();
    existingUser.setName("oldName");
    existingUser.setEmail("old@test.org");
    existingUser.setPasswordHash("encodedPass");

    when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
    when(passwordEncoder.matches("testpass1", "encodedPass")).thenReturn(true);
    when(userRepository.save(existingUser)).thenReturn(existingUser);

    // Act
    UserDTO result = userService.updateUser(userViewModel);

    // Assert
    verify(userRepository, times(1)).findById(1L);
    verify(passwordEncoder, times(1)).matches("testpass1", "encodedPass");
    verify(userRepository, times(1)).save(existingUser);
    assertNotNull(result);
    // Confirm that the fields were updated
    assertEquals("updatedName", existingUser.getName());
    assertEquals("updated@test.org", existingUser.getEmail());
    // The password hash remains unchanged since it matched
    assertEquals("encodedPass", existingUser.getPasswordHash());
  }

  // Test for updateUser when the password does not match (thus re-encoding)
  @Test
  public void testUpdateUserPasswordDoesNotMatch() {
    // Arrange: Create a view model with an idHash for user id 1
    UserViewModel userViewModel = new UserViewModel();
    userViewModel.setName("updatedName");
    userViewModel.setEmail("updated@test.org");
    userViewModel.setPassword("newpass");
    String idHash = Base64.getEncoder().encodeToString("1".getBytes());
    userViewModel.setIdHash(idHash);

    // Arrange: Existing user with an old password hash
    User existingUser = new User();
    existingUser.setName("oldName");
    existingUser.setEmail("old@test.org");
    existingUser.setPasswordHash("oldEncodedPass");

    when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
    // Simulate that the password does not match
    when(passwordEncoder.matches("newpass", "oldEncodedPass")).thenReturn(false);
    // Simulate new password encoding
    when(passwordEncoder.encode("newpass")).thenReturn("newEncodedPass");
    when(userRepository.save(existingUser)).thenReturn(existingUser);

    // Act
    UserDTO result = userService.updateUser(userViewModel);

    // Assert
    verify(userRepository, times(1)).findById(1L);
    verify(passwordEncoder, times(1)).matches("newpass", "oldEncodedPass");
    verify(passwordEncoder, times(1)).encode("newpass");
    verify(userRepository, times(1)).save(existingUser);
    assertNotNull(result);
    // Confirm that the fields have been updated and password hash changed
    assertEquals("updatedName", existingUser.getName());
    assertEquals("updated@test.org", existingUser.getEmail());
    assertEquals("newEncodedPass", existingUser.getPasswordHash());
  }

  // Test for updateUser when the user is not found
  @Test
  public void testUpdateUserUserNotFound() {
    // Arrange
    UserViewModel userViewModel = new UserViewModel();
    userViewModel.setName("updatedName");
    userViewModel.setEmail("updated@test.org");
    userViewModel.setPassword("testpass1");
    String idHash = Base64.getEncoder().encodeToString("1".getBytes());
    userViewModel.setIdHash(idHash);

    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    // Act
    UserDTO result = userService.updateUser(userViewModel);

    // Assert
    verify(userRepository, times(1)).findById(1L);
    assertNull(result);
  }

  // Test for deleteUser
  @Test
  public void testDeleteUser() {
    // Arrange: Use an id of 1 and create its idHash
    String id = "1";
    String idHash = Base64.getEncoder().encodeToString(id.getBytes());

    // Act: Call deleteUser
    userService.deleteUser(idHash);

    // Assert: Verify that deleteById was called with the correct id
    verify(userRepository, times(1)).deleteById(1L);
  }
}
