package com.example.myapplication.utils;

public class Validator {
    public static boolean validateInputs(String usr, String pwd) {

        return !usr.isEmpty() && !pwd.isEmpty();
    }

    public static boolean login(String usr, String pwd) {

        return true;
    }




    public static String validateRegisterInputs(String username, String password, String rePassword) {
        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            return "empty";
        }

        if (password.length() < 6) {
            return "pwd_invalid";
        }

        if (!password.equals(rePassword)) {
            return "re_pwd_incorrect";
        }

        return "success";
    }

}
