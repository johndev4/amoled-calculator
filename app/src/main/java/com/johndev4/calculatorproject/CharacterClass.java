package com.johndev4.calculatorproject;

/**
 *
 * @author johndev4
 **/

class CharacterClass {

    /*
     * Count the specified character inside the string.
     * Parameters:
     *      * char character - the character would be duplicate
     *      * String inString - string that might contains the character
     */
    static int count(char character, String inString) {
        int cnt = 0;
        for (int i = 0; i < inString.length(); i++){
            if (inString.charAt(i) == character){
                cnt += 1;
            }
        }
        return cnt;
    }

    /*
     * Duplicate the specified character with specified no. of copies.
     * Parameters:
     *      * char character - the character would be duplicate
     *      * int cnt - number of copies
     */
    static  String duplicate(char character, int cnt) {
        StringBuilder characterSequence = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            characterSequence.append(character);
        }
        return characterSequence.toString();
    }

    /*
     * Removes character in a string.
     * Parameters:
     *      * char character - the character would be remove in string
     *      * String inString - the string that might contain the character
     */
    static String remove(char character, String inString) {
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < inString.length(); i++){
            if (inString.charAt(i) != character){
                temp.append(inString.charAt(i));
            }
        }

        return temp.toString();
    }

    /*
     * Removes positive sign for every ScientificNotation in String parameter.
     * Parameters:
     *      * String str - Scientific Notation
     */
    static String removePositiveSign(String str) {
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) != '+' || !temp.toString().endsWith("E")){
                temp.append(str.charAt(i));
            }
        }

        return temp.toString();
    }

    /*
     * Removes operator at last position to evaluate the expression in Javaluator.
     * Parameters:
     *      * String str - the expression
     */
    static String removeLatterOperator(String str) {
        if (str.endsWith(" + ")){
            str = replaceTarget(str, " + "+"@last", "");

        } else if (str.endsWith(" - ")){
            str = replaceTarget(str, " - "+"@last", "");

        } else if (str.endsWith(" * ")){
            str = replaceTarget(str, " * "+"@last", "");

        } else if (str.endsWith(" / ")){
            str = replaceTarget(str, " / "+"@last", "");

        } else if (str.endsWith(" % ")){
            str = replaceTarget(str, " % "+"@last", "");

        }

        return str;
    }

    /*
     * Removes replace all illegal operators to make valid the expression using Javaluator.
     * Parameters:
     *      * String str - the expression
     */
    static String replaceIllegalOperator(String str) {
        str = str.replaceAll(" + ", " + ");
        str = str.replaceAll(" − ", " - ");
        str = str.replaceAll(" × ", " * ");
        str = str.replaceAll(" ÷ ", " / ");
        str = str.replaceAll(" Mod ", " % ");

        return str;
    }

    /*
     * Replace the target in a string. The target position might be determined "@last" (replace only the target
     * in last part of string) or "@first" (replace only the target in last part of string) else it will replace
     * all the target inside the str parameter.
     * Parameters:
     *      * String inString - the string which might contain the target
     *      * String target -  the target that will be replaced
     *      * String replacement - the value to be replaced.
     */
    static String replaceTarget(String inString, String target, String replacement) {
        String pos = target.contains("@") ? target.substring(target.indexOf('@')) : "";
        target = target.contains("@") ? target.substring(0, target.indexOf('@')) : target;
        String newStr;

        switch (pos) {
            case "@first":
                newStr = inString.replaceFirst(target, replacement);
                break;

            case "@last":
                if (inString.contains(target)) {
                    int targetLength = target.length();
                    int strLength = inString.length();
                    newStr = inString.substring(0, strLength - targetLength);
                    newStr += replacement;
                } else{
                    newStr = inString;
                }
                break;

            default:
                newStr = inString.replaceAll(target, replacement);
        }
        return newStr;
    }

    /*
     * Test the expression if it is ends with operator.
     * Parameters:
     *      * String exp = expression
     */
    static boolean endsWithOperator(String exp) {
        if (exp.endsWith(" + ")){
            return true;

        } else if (exp.endsWith(" - ")){
            return true;

        } else if (exp.endsWith(" * ")){
            return true;

        } else if (exp.endsWith(" / ")){
            return true;

        } else if (exp.endsWith(" % ")){
            return true;

        }

        return false;
    }
}
