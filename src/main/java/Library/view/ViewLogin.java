package Library.view;

import Library.controller.LoginController;

public interface ViewLogin {
    String getLogin();
    String getPassword();
    void showError(String message);
    void setController(LoginController c);
    void close();
}