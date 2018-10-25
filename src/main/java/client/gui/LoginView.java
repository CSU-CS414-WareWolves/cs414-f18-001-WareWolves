package client.gui;

import java.io.IOException;

public interface LoginView {
  // Print a nice splash title for our game
  void showSplash() throws IOException;

  // Print the login menu options
  void showLogin();
}
