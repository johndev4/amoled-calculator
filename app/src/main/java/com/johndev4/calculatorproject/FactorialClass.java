package com.johndev4.calculatorproject;

import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;

/**
 *
 * @author johndev4
 */

class FactorialClass {
    
    /*
     * Return the factorial value of the number (eg. fact(5) = 120, solution: 5x4x3x2x1 = 120). If the
     * number is greater than 999,999,999 then it will return NaN. Else, if the number is less than
     * 999,999,999 but greater than 170 then it will return INFINITY. (This will prevent the long
     * process in the system when the number is too large)
     * Parameters:
     *      * double d - the number
     */
    static double fact(double d) {
        if (d > 999999999){
            return NaN;
            
        } else if (d > 170){
            return POSITIVE_INFINITY;
            
        } else{
            double fact = 1;
            for (int i = 1; i <= d; i++){
                fact *= i;
            }
            return fact;    
        }
    }
}
