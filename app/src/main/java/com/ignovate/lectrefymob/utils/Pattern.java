package com.ignovate.lectrefymob.utils;


public class Pattern  {

    /**
     * Do not create this static utility class.
     */
    public static final String PASSWORD_VALIDE ="(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%&*()_+=|<>?{}\\[\\]~-]).{8,15}";
    public static final String MOBILE_VALIDE ="^[+]?[0-9]{10,13}$";
}
