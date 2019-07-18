package com.johndev4.calculatorproject;

import static com.johndev4.calculatorproject.CharacterClass.endsWithOperator;
import static com.johndev4.calculatorproject.CharacterClass.remove;
import static com.johndev4.calculatorproject.CharacterClass.removeLatterOperator;
import static com.johndev4.calculatorproject.CharacterClass.removePositiveSign;
import static com.johndev4.calculatorproject.CharacterClass.replaceIllegalOperator;

/**
 *
 * @author johndev4
 **/

class ModifiedDoubleEvaluator {

    /*
     * Evaluate of all expression except the character operator at last.
     * Parameters:
     *      * String expression - the expression
     *      * String angle - angle for functions such as sin, tan, & cos
     */
    private static double evalAll(String expression, String angle) {
        ExtendedDoubleEvaluator evaluator = new ExtendedDoubleEvaluator(angle);
        return evaluator.evaluate(removeLatterOperator(expression));
    }

    /*
     * Evaluate of latter part of  expression except the character operator at last. It will begin
     * from after the second-to-the-last operator until before the last operator
     * Parameters:
     *      * String expression - the expression
     *      * String angle - angle for functions such as sin, tan, & cos
     */
    private static double evalast(String exp, String angle) {
        exp = removeLatterOperator(exp);
        StringBuilder tempBuilder = new StringBuilder();

        for (int i = exp.length()-1; i >= 0; i--){
            if (exp.charAt(i) == '-' ||
                    exp.charAt(i) == '+' ||
                    exp.charAt(i) == '/' ||
                    exp.charAt(i) == '*' ||
                    exp.charAt(i) == '%')
            {
                break;
            }

            tempBuilder.insert(0, exp.charAt(i));
        }

        return evalAll(tempBuilder.toString(), angle);
    }

    /*
     * Replace operator with legal symbol, remove comma, remove positive sign in scientific(Eg. 3.44E+12 to 3.44E12).
     * Parameters:
     *      * String expression - expression that might illegal for javaluator
     */
    private static String clean(String expression) {
        return replaceIllegalOperator(remove(',', removePositiveSign(expression)));
    }

    /*
     * Evaluate the expression according to its precedence.
     * Parameters:
     *      * String expression - the expression
     *      * String angle - angle for functions such as sin, tan, & cos
     */
    static double eval(String expression, String angle) {
        expression = clean(expression);

        char[] op0 = {'+', '-'};
        char[] op1 = {'*', '/', '%'};

        final int def = 2;
        int lOp = def, fOp = def;
        int i = expression.length() - 1;
        double result;

        if (expression.endsWith("^")){
            expression = expression.substring(0, expression.length()-1);
            return evalast(expression, angle);
        } else if (endsWithOperator(expression)){
            while ((lOp == def || fOp == def) && i >= 0){
                if (expression.charAt(i) == '-' ||
                        expression.charAt(i) == '+' ||
                        expression.charAt(i) == '/' ||
                        expression.charAt(i) == '*' ||
                        expression.charAt(i) == '%')
                {
                    int rank = def;

                    for (char j : op0){
                        if (expression.charAt(i) == j){
                            rank = 0;
                        }
                    }

                    for (char j : op1){
                        if (expression.charAt(i) == j){
                            rank = 1;
                        }
                    }

                    if (lOp == def){
                        lOp = rank;

                    } else{
                        fOp = rank;
                    }
                }

                i--;
            }

            if (lOp > fOp){
                result = evalast(expression, angle);

            } else{
                result = evalAll(expression, angle);
            }
        } else{
            result = evalAll(expression, angle);
        }

        return result;
    }
}