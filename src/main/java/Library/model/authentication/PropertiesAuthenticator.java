package Library.model.authentication;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesAuthenticator extends Authenticator {
    private final String path;

    public PropertiesAuthenticator() {
        this.path = "src/main/resources/users.properties";
    }

    public PropertiesAuthenticator(String path) {
        this.path = path;
    }

    @Override
    protected boolean isLoginExists(String login) {
        return load().containsKey(login);
    }

    @Override
    protected String getPassword(String login) {
        String data = load().getProperty(login);
        if (data == null) return null;
        return data.split(";")[0];
    }

    @Override
    public String getRole(String login) {
        String data = load().getProperty(login);
        if (data == null) return null;
        String[] parts = data.split(";");
        return parts.length >= 2 ? parts[1] : null;
    }

    private boolean adminExists() {
        Properties props = load();
        for (Object value : props.values()) {
            String[] parts = value.toString().split(";");
            if (parts.length >= 2 && "admin".equals(parts[1])) {
                return true;
            }
        }
        return false;
    }

    public boolean saveUser(String login, String password) {
        Properties props = load();
        if (props.containsKey(login)) return false;

        String role;
        if(login.equals("Admin"))
        {
            boolean verif= adminExists();
            if(verif==true)
            {
                return false;
            }
            else {
                role="admin";
            }
        }
        else {
            role = "membre";
        }

        props.setProperty(login, password + ";" + role);
        return sauvegarder(props);
    }

    private boolean sauvegarder(Properties props) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            props.store(fos, "Library users");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Properties load() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        } catch (Exception ignored) {}
        return props;
    }
}