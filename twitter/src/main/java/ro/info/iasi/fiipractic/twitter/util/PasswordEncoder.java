package ro.info.iasi.fiipractic.twitter.util;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder implements IPasswordEncoder{

    private final BCryptPasswordEncoder passwordEncoder;


    public PasswordEncoder() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword.trim(), hashedPassword.trim());
    }
}