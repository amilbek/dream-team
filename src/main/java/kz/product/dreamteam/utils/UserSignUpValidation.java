package kz.product.dreamteam.utils;

import kz.product.dreamteam.model.dto.SignUpRequest;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSignUpValidation {

    private static final String EMAIL_PATTERN;
    private static final String PASSWORD_PATTERN;

    static {
        EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
        PASSWORD_PATTERN = "[a-zA-Z0-9~!@#$%^&*]{8,20}$";
    }

    private UserSignUpValidation() {
    }

    public static boolean isValidBirthDate(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        int age = Period.between(birthDate, now).getYears();
        return age >= 18;
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidPasswordConfirmation(SignUpRequest signUpRequest) {
        return signUpRequest.getPassword().equals(signUpRequest.getPasswordConfirmation());
    }
}
