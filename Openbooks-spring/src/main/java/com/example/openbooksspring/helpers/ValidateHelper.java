package com.example.openbooksspring.helpers;

import java.util.regex.Pattern;

public class ValidateHelper {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String login) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(login).find();
    }
}
