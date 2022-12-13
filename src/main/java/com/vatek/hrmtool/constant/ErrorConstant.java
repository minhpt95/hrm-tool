package com.vatek.hrmtool.constant;

public class ErrorConstant {
    public static class Code {
        public static final String SUCCESS = "00";
        public static final String LOGIN_INVALID = "01";
        public static final String USER_INACTIVE = "02";
        public static final String NOT_FOUND = "03";
        public static final String ALREADY_EXISTS = "04";
        public static final String PERMISSION_DENIED = "05";


        public static final String INTERNAL_SERVER_ERROR = "06";

        public static final String TOKEN_REFRESH_EXCEPTION = "07";

        public static final String MISSING_FIELD = "08";
    }

    public static class Type {
        public static final String LOGIN_INVALID = "LOGIN_INVALID";
        public static final String USER_INACTIVE = "USER_INACTIVE";
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILURE = "FAILURE";
        public static final String NOT_FOUND = "NOT_FOUND";
        public static final String PERMISSION_DENIED = "PERMISSION_DENIED";
        public static final String MISSING_FIELD = "MISSING_FIELD";
        public static final String TOKEN_REFRESH_EXCEPTION = "TOKEN_REFRESH_EXCEPTION";

        public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    }

    public static class Message {
        public static final String LOGIN_INVALID = "Username or password invalid.";
        public static final String USER_INACTIVE = "User inactive.";
        public static final String SUCCESS = "SUCCESS.";
        public static final String ALREADY_EXISTS = "%s already exists.";
        public static final String NOT_FOUND = "%s not found.";
        public static final String BLANK =" %s not blank.";
        public static final String END_OF_TIME ="Time activate expired";
    }
}
