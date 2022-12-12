package com.vatek.hrmtool.constant;

public class ErrorConstant {
    public static class Code {
        public static final String SUCCESS = "00";
        public static final String LOGIN_INVALID = "01";
        public static final String USER_INACTIVE = "02";
        public static final String NOT_FOUND = "03";
        public static final String ALREADY_EXISTS = "04";
        public static final String PERMISSION_DENIED = "05";
    }

    public static class Type {
        public static final String LOGIN_INVALID = "LOGIN_INVALID";
        public static final String USER_INACTIVE = "USER_INACTIVE";
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILURE = "FAILURE";
        public static final String NOT_FOUND = "NOT FOUND";

    }

    public static class Message {
        public static final String LOGIN_INVALID = "Username or password invalid.";
        public static final String USER_INACTIVE = "User inactive.";
        public static final String SUCCESS = "SUCCESS.";
        public static final String ALREADY_EXISTS = "%s already exists.";
        public static final String NOT_EXISTS = "%s not exist.";

        public static final String NOT_FOUND = "%s not found.";
        public static final String BLANK =" %s not blank.";
        public static final String END_OF_TIME ="Time activate expired";
    }
}
