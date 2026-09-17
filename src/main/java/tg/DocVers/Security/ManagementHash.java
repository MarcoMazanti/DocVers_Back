package tg.DocVers.Security;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class ManagementHash {
    public static String encriptarHash(String texto) {
        return BCrypt.hashpw(texto, BCrypt.gensalt());
    }

    public static boolean validarHash(String textoPuro, String textoHash) {
        return BCrypt.checkpw(textoPuro, textoHash);
    }
}
