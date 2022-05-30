package com.ifsc.secstor.api.advice.messages;

public class ErrorMessages {

    public static final String USER_NOT_FOUND = "User not found with provided credentials";
    public static final String USER_ALREADY_REGISTERED = "User is already registered";
    public static final String INVALID_ROLE = "Role provided is invalid, it must be either CLIENT or ADMINISTRATOR";
    public static final String VALIDATION_ERROR = "Validation Error";
    public static final String NULL_USERNAME = "Username is missing";
    public static final String NULL_PASSWORD = "Password is missing";
    public static final String AUTHENTICATION_ERROR = "Authentication Error";
    public static final String NOT_AUTHENTICATED = "Authentication is needed to access this resource";
    public static final String AUTH_ERROR = "Authorization Error";
    public static final String NULL_AUTH_HEADER = "Authorization header is missing";
    public static final String INSUFFICIENT_PERMISSION = "Insufficient permission to access this resource";
    public static final String INVALID_AUTH_HEADER = "Authorization header is invalid";
    public static final String NULL_BODY = "Body must be provided";
    public static final String INVALID_BODY = "Body provided is invalid, it must be an object";
    public static final String NULL_DATA = "Data must be provided";
    public static final String INVALID_DATA = "Data provided is invalid, it must be an object";
    public static final String INVALID_DATA_ARRAY = "Data provided is invalid, it must be an array of objects";
    public static final String INVALID_KEYSET = "Keysets of the objects are different";
    public static final String NULL_ALGORITHM = "Algorithm must be provided";
    public static final String INVALID_ALGORITHM = "Algorithm provided is invalid, it must be either SHAMIR, PSS, CSS, KRAWCZYK or PVSS";
    public static final String NULL_SECRET = "Secret must be provided";
    public static final String INVALID_SECRET = "Secret provided is invalid, it must be an object";
    public static final String NO_MATCH_SECRET = "Secret provided doesn't match any of share algorithm types";
    public static final String NOT_ENOUGH_SHARES = "Not enough shares to reconstruct the secret, it must be at least 5";
    public static final String MISSING_MAC_KEYS = "There was a missing parameter: MACKEYS";
    public static final String MISSING_MACS = "There was a missing parameter: MACS";
    public static final String MISSING_FINGERPRINTS = "There was a missing parameter: FINGERPRINTS";
    public static final String MISSING_ENCKEYS = "There was a missing parameter: ENCKEYS";
    public static final String MISSING_ENCALGORITHM = "There was a missing parameter: ENCALGORITHM";
    public static final String MISSING_ORIGINALLENGTH = "There was a missing parameter: ORIGINALLENGTH";
    public static final String NULL_ATTRIBUTE_CONFIG = "Attribute config must be provided";
    public static final String INVALID_ATTRIBUTE_CONFIG = "Attribute config provided is invalid, it must be an object";
    public static final String INVALID_GENERALIZATION_STRING_LENGTH = "All strings must have the same length using generalization";
    public static final String INVALID_SIMILARITY_LEVEL = "In order to use generalization at least the first character of the strings must be equal";

    public static String INVALID_PARAMETER(Object field) {
        return "Parameter " + field.toString().toUpperCase() + " is invalid, it must be an object";
    }

    public static final String INVALID_GENERALIZATION_LEVEL = "Value provided for generalization level is invalid, it must be an integer number";
    public static final String NULL_CLASSIFICATION =  "Every parameter must have a classification, the options are: identifying, sensitive or quasi-identifier";

    public static String INVALID_CLASSIFICATION(Object field) {
        return "Classification provided for field: " + field.toString().toUpperCase() + " is invalid, it must be either identifier, sensitive or quasi-identifier";
    }

    public static String NULL_METHOD(Object field) {
        return "Missing parameter METHOD for field: " + field.toString().toUpperCase() + " , it must be either generalization or randomization";
    }

    public static String INVALID_METHOD(Object field) {
        return "Method provided for field: " + field.toString().toUpperCase() + " is invalid, it must be either generalization or randomization";
    }

    public static final String NULL_ROLE = "Role is missing";
    public static final String INVALID_USERNAME_LENGTH = "Username must be between 5 and 20 characters";
    public static final String INVALID_USERNAME = "Username can only contain lower case letters, numbers, underscore, dash or dot with no white spaces";
    public static final String INVALID_PASSWORD = "Password must include at least one uppercase and lowercase letters, a number and a symbol with no white spaces";
    public static final String INVALID_PASSWORD_LENGTH = "Password must be between 8 and 12 characters";
}
