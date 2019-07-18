package com.johndev4.calculatorproject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static java.lang.Character.isDigit;
import static java.lang.Double.parseDouble;
import static java.lang.Math.pow;
import static java.lang.Math.round;

/**
 *
 * @author johndev4
 **/

class NumberClass {

    private static NumberFormat formatter = new DecimalFormat("#,###.###############################################");
    private static final int NONE = -1; //default

    /*
     * Rebuild the number to real number wit h NumberFormat.
     * Parameters:
     *      * String str - the number in String type
     *      * int maxLength - the max length of the number's digit
     *      * int cnValue - max decimal place if the number is Scientific Notation
     */
    static String rebuild(String str, int maxLength, int cnValue){
        return rebuild(toDouble(str), maxLength, cnValue);
    }

    /*
     * Rebuild the number to real number with NumberFormat.
     * Parameters:
     *      * double a - the number in double type
     *      * int maxLength - the max length of the number's digit
     *      * int cnValue - max decimal place if the number is Scientific Notation
     */
    static String rebuild(double a, int maxLength, int cnValue) {
        String str = formatter.format(a);

        if (length(str) > maxLength && !isRational(str)){
            return compressNotation(toString(toDouble(str)), cnValue);
        }

        else if (length(str) > maxLength && isRational(str) && (toDouble(str) >= 1)){
            int p1 = 0;
            int p2;

            for (int i = 0; i < str.length(); i++){
                if (str.charAt(i) == '.'){
                    break;
                }
                else if (str.charAt(i) != ','){
                    p1++;
                }
            }
            p2 = length(str) - p1;
            int other = length(str) - maxLength;
            int dp = p2 - other;
            return compressDecimal(str, dp);
        }

        else if (length(str) > maxLength && isRational(str) && (a < (9/pow(10, maxLength)) && toDouble(str) > 0)){
            return compressNotation(toString(a), cnValue);
        }

        else if (length(str) > maxLength && isRational(str) && (toDouble(str) < 1 && toDouble(str) > 0)){
            int p1 = 0;
            int p2;

            for (int i = 0; i < str.length(); i++){
                if (str.charAt(i) == '.'){
                    break;
                }
                else if (str.charAt(i) != ','){
                    p1++;
                }
            }
            p2 = length(str) - p1;
            int other = length(str) - maxLength;
            int dp = p2 - other;
            return compressDecimal(str, dp);
        }

        return str;
    }

    /*
     * Round off the rational number.
     * Parameters:
     *      * String str - the number
     *      * int decimalPlace - nearest decimal place
     */
    private static String compressDecimal(String str, int decimalPlace) {
        if (decimalPlace != -1){
            StringBuilder pattern = new StringBuilder(",###.");
            for (int i = 0; i < decimalPlace; i++){
                pattern.append('#');
            }

            formatter = new DecimalFormat(pattern.toString());
            str = formatter.format(toDouble(str));

        }
        return str;
    }

    /*
     * Compress the length of Scientific Notation.
     * Parameters:
     *      * String str - Scientific Notation
     *      * int decimalPlace - nearest decimal place
     */
    private static String compressNotation(String str, int decimalPlace) {
        if (isSciNotation(str)){
            String a = str.substring(0, str.indexOf("E"));
            String b = str.substring(str.indexOf("E"));
            if (decimalPlace != NONE){
                a = toString(round(parseDouble(a) * pow(10,decimalPlace))/pow(10,decimalPlace));
            }

            return a+b;
        }

        return str;
    }

    /*
     * Convert double to string.
     * Parameters:
     *      * double a - number
     */
    private static String toString(double a) {
        String str = a+"";
        if(str.endsWith(".0")){
            return (int)a+"";
        }

        if (isSciNotation(str)){
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < str.length(); i++){
                temp.append(str.charAt(i));
                if (str.charAt(i) == 'E' && str.charAt(i+1) != '-'){
                    temp.append("+");
                }
            }
            return temp.toString();
        }
        return str;
    }

    /*
     * Convert string to double.
     * Parameters:
     *      * String str - the number
     */
    private static double toDouble(String str) {
        StringBuilder temp = new StringBuilder();
        if (str.isEmpty()){
            temp = new StringBuilder("0");
        } else{
            for (int i = 0; i < str.length(); i++){
                if (str.charAt(i) != ','){
                    temp.append(str.charAt(i));
                }
            }
        }

        return parseDouble(temp.toString());
    }

    /*
     * Return the length of digit in a number
     * Parameters:
     *      * String str - the number
     */
    static int length(String str) {
        int len = 0;

        for (int i = 0; i < str.length(); i++){
            if (isDigit(str.charAt(i))){
                len++;
            }
        }
        return len;
    }

    /*
     * Test the number if Rational.
     * Parameters:
     *      * String str - the number
     */
    private static boolean isRational(String str) {
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '.' && !isSciNotation(str)){
                return true;
            }
        }
        return false;
    }

    /*
     * Test the number if Scientific Notation
     * Parameters:
     *      * String str - the number
     */
    private static boolean isSciNotation(String str) {
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == 'E'){
                return true;
            }
        }
        return false;
    }
}
