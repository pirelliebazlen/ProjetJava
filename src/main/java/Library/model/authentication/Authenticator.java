package Library.model.authentication;

/**
 * Classe abstraite définissant le squelette de l'authentification
 * selon le patron de conception <b>Template Method</b>.
 * <p>
 * Les sous-classes doivent fournir l'implémentation concrète
 * de la vérification du login et de la récupération du mot de passe.
 */
public abstract class Authenticator {
    //template method
    /**
     * Méthode template : vérifie si le couple login/mot de passe est valide.
     *
     * @param username le login de l'utilisateur
     * @param password le mot de passe à vérifier
     * @return {@code true} si l'authentification réussit, {@code false} sinon
     */
    public boolean authenticate(String username, String password) {
        if (isLoginExists(username))
        {
            String storedPassword = getPassword(username);
            return storedPassword != null && storedPassword.equals(password);
        }
        return false;
    }
    
    protected abstract boolean isLoginExists(String username);

    /**
     * Récupère le mot de passe associé à un login.
     *
     * @param username le login
     * @return le mot de passe stocké, ou {@code null} si introuvable
     */
    protected abstract String getPassword(String username);

    /**
     * Récupère le rôle associé à un login (ex: "admin", "membre").
     *
     * @param login le login
     * @return le rôle de l'utilisateur, ou {null} si introuvable
     */
    public abstract String getRole(String login);
}