package tg.DocVers.Security;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {
    private static final PasswordGenerator generator = new PasswordGenerator();

    public static String gerarToken(String prefix) {
        // Regras de obrigatoriedade
        CharacterRule upper = new CharacterRule(EnglishCharacterData.UpperCase, 1);
        CharacterRule lower = new CharacterRule(EnglishCharacterData.LowerCase, 1);
        CharacterRule digit = new CharacterRule(EnglishCharacterData.Digit, 1);
        CharacterRule special = new CharacterRule(new CharacterData() {
            @Override
            public String getErrorCode() { return "INSUFFICIENT_SPECIAL"; }
            @Override
            public String getCharacters() { return "!@$%^&*()-_=+[{}|;:,.<>]"; }
        }, 1);

        String randomPart = generator.generatePassword(35, upper, lower, digit, special);
        return prefix + randomPart;
    }
}
