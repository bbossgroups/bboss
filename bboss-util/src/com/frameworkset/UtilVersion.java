package com.frameworkset;

import java.util.Date;

public class UtilVersion {
	public static final String description="1.0.0";
    public static final short version=100;
    public static final Date releaseDate = new Date("2008/08/12");

    /**
     * Prints the value of the description and cvs fields to System.out.
     * @param args
     */
    public static void main(String[] args) {
    	  System.out.println("\nVersion: \t" + description + "\nDate:\t" + releaseDate);
       
        
    }

    /**
     * Returns the catenation of the description and cvs fields.
     * @return String with description
     */
    public static String printDescription() {
        return "Poolman " + description ;
    }

    /**
     * Returns the version field as a String.
     * @return String with version
     */
    public static String printVersion() {
        return Short.toString(version);
    }

    /**
     * Compares the specified version number against the current version number.
     * @param v short
     * @return Result of == operator.
     */
    public static boolean compareTo(short v) {
        return version == v;
    }

}
