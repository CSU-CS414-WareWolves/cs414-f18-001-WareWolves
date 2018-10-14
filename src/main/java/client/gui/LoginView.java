package client.gui;

public interface LoginView {
  // Print a nice splash title for our game
  void showSplash();

  // Log in an existing user
  void login(String email, String password);

  // Registers a new user
  void register(String email, String password, String nickname);
}
