package com.spribe.dataproviders;

import org.testng.annotations.DataProvider;

public class PasswordDataProvider {
    @DataProvider(name = "invalidPasswords", parallel = true)
    public Object[][] invalidPasswords() {
        return new Object[][]{
                {"pass!word"},
                {"hello@123"},
                {"Admin#2025"},
                {"qwerty$"},
                {"test%test"},
                {"my pass"},
                {"pass\t123"},
                {"123\n456"},
                {"пароль123"},
                {"pässword123"},
                {"contraseña2025"},
                {"qwertyu"},
                {"2784635"},
        };
    }
}
