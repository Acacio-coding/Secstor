package com.ifsc.secstor.api.advice.paths;

public class Paths {

    public static final String SECRET_SHARING_BASE = "/api/v1/secret-sharing";
    public static final String SECRET_SHARING_SPLIT = "/split";
    public static final String SECRET_SHARING_BASE_AND_SPLIT = SECRET_SHARING_BASE + SECRET_SHARING_SPLIT;
    public static final String SECRET_SHARING_RECONSTRUCT = "/reconstruct";
    public static final String SECRET_SHARING_BASE_AND_RECONSTRUCT = SECRET_SHARING_BASE + SECRET_SHARING_RECONSTRUCT;

    public static final String DATA_ANONYMIZATION_BASE = "/api/v1/data-anonymization";
    public static final String DATA_ANONYMIZATION_ANONYMIZE = "/anonymize";
    public static final String DATA_ANONYMIZATION_BASE_AND_ANONYMIZE = DATA_ANONYMIZATION_BASE + DATA_ANONYMIZATION_ANONYMIZE;


    public static final String USER_BASE = "/api/v1";
    public static final String USERS = "/users";
    public static final String USER = "/user/{username}";
    public static final String USER_PATH = "/api/v1/user/";
    public static final String SAVE_USER = "/user/save";
    public static final String SAVE_USER_AUTH = "/api/v1/user/save";
    public static final String SAVE_USER_PATH = "/api/v1/user/save";
    public static final String REFRESH_TOKEN = "/token/refresh";
    public static final String REGISTER_BASE = "/v1";
    public static final String REGISTER = "/register";


    public static final String LOGIN_ROUTE = "/api/v1/login";
    public static final String REFRESH_TOKEN_ROUTE = "/api/v1/token/refresh/**";
    public static final String REFRESH_TOKEN_ROUTE_AUTH = "/api/v1/token/refresh";
    public static final String SAVE_USER_ROUTE = "/api/v1/user/save/**";
    public static final String SPLIT_ROUTE = "/api/v1/secret-sharing/split/**";
    public static final String RECONSTRUCT_ROUTE = "/api/v1/secret-sharing/reconstruct/**";
    public static final String ANONYMIZATION_ROUTE = "/api/v1/data-anonymization/anonymize/**";
    public static final String GET_USERS_ROUTE = "/api/v1/users/**";
    public static final String USER_ROUTE = "/api/v1/user/**";
    public static final String REGISTER_ROUTE = "/v1/register/**";
    public static final String REGISTER_ROUTE_AUTH = "/v1/register";
    public static final String STATIC_ROUTE = "/static/**";
    public static final String CSS_ROUTE = "/css/**";
    public static final String JS_ROUTE = "/js/**";
    public static final String IMG_ROUTE = "/img/**";
}
