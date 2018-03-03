package com.atk.mail.base;

import java.io.Serializable;
import java.util.Locale;

public class SharedUtil implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5269458139792419736L;

    public static boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }

        return o1.equals(o2);
    }

 
    public static String trimTrailingSlashes(String value) {
        return value.replaceAll("/*$", "");
    }

   
    public static final String SIZE_PATTERN = "^(-?\\d*(?:\\.\\d+)?)(%|px|em|rem|ex|in|cm|mm|pt|pc)?$";

    
    public static String[] splitCamelCase(String camelCaseString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camelCaseString.length(); i++) {
            char c = camelCaseString.charAt(i);
            if (Character.isUpperCase(c) && isWordComplete(camelCaseString, i)) {
                sb.append(' ');
            }
            sb.append(c);
        }
        return sb.toString().split(" ");
    }

    private static boolean isWordComplete(String camelCaseString, int i) {
        if (i == 0) {
            // Word can't end at the beginning
            return false;
        } else if (!Character.isUpperCase(camelCaseString.charAt(i - 1))) {
            // Word ends if previous char wasn't upper case
            return true;
        } else if (i + 1 < camelCaseString.length()
                && !Character.isUpperCase(camelCaseString.charAt(i + 1))) {
            // Word ends if next char isn't upper case
            return true;
        } else {
            return false;
        }
    }

    public static String camelCaseToHumanFriendly(String camelCaseString) {
        String[] parts = splitCamelCase(camelCaseString);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = capitalize(parts[i]);
        }
        return join(parts, " ");
    }

    @SuppressWarnings("unused")
	private static boolean isAllUpperCase(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (!Character.isUpperCase(c) && !Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static String join(String[] parts, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            sb.append(separator);
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    public static String capitalize(String string) {
        if (string == null) {
            return null;
        }

        if (string.length() <= 1) {
            return string.toUpperCase();
        }

        return string.substring(0, 1).toUpperCase(Locale.ENGLISH)
                + string.substring(1);
    }

   
    public static String propertyIdToHumanFriendly(Object propertyId) {
        String string = propertyId.toString();
        if (string.isEmpty()) {
            return "";
        }

        // For nested properties, only use the last part
        int dotLocation = string.lastIndexOf('.');
        if (dotLocation > 0 && dotLocation < string.length() - 1) {
            string = string.substring(dotLocation + 1);
        }

        return camelCaseToHumanFriendly(string);
    }

   
    public static String addGetParameters(String uri, String extraParams) {
        if (extraParams == null || extraParams.length() == 0) {
            return uri;
        }
        // RFC 3986: The query component is indicated by the first question
        // mark ("?") character and terminated by a number sign ("#") character
        // or by the end of the URI.
        String fragment = null;
        int hashPosition = uri.indexOf('#');
        if (hashPosition != -1) {
            // Fragment including "#"
            fragment = uri.substring(hashPosition);
            // The full uri before the fragment
            uri = uri.substring(0, hashPosition);
        }

        if (uri.contains("?")) {
            uri += "&";
        } else {
            uri += "?";
        }
        uri += extraParams;

        if (fragment != null) {
            uri += fragment;
        }

        return uri;
    }

    public static String dashSeparatedToCamelCase(String dashSeparated) {
        if (dashSeparated == null) {
            return null;
        }
        String[] parts = dashSeparated.split("-");
        for (int i = 1; i < parts.length; i++) {
            parts[i] = capitalize(parts[i]);
        }

        return join(parts, "");
    }


	public static String upperCaseUnderscoreToHumanFriendly(String name) {
		return null;
	}

}
