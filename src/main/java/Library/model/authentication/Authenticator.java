package Library.model.authentication;

public abstract class Authenticator {
    //template method
    public boolean authenticate(String username, String password) {
        if (isLoginExists(username))
        {
            String storedPassword = getPassword(username);
            return storedPassword != null && storedPassword.equals(password);
        }
        return false;
    }
    protected abstract boolean isLoginExists(String username);
    protected abstract String getPassword(String username);
    public abstract String getRole(String login);
}