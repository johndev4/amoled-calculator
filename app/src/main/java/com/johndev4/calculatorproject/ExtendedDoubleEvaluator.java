package com.johndev4.calculatorproject;

import com.fathzer.soft.javaluator.Constant;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Parameters;

import org.apache.commons.math3.util.FastMath;

import java.util.Iterator;

/**
 *
 * @author johndev4
 **/

/* Extend new Functions that use FastMath Class instead of Math Class.
 */
public class ExtendedDoubleEvaluator extends DoubleEvaluator {
    /** Defines the new functions.*/
    private static final Function SIN = new Function("sin", 1);
    private static final Function ASIN = new Function("asin", 1);
    private static final Function COS = new Function("cos", 1);
    private static final Function ACOS = new Function("acos", 1);
    private static final Function TAN = new Function("tan", 1);
    private static final Function ATAN = new Function("atan", 1);
    
    private static final Function SINH = new Function("sinh", 1);
    private static final Function ASINH = new Function("asinh", 1);
    private static final Function COSH = new Function("cosh", 1);
    private static final Function ACOSH = new Function("acosh", 1);
    private static final Function TANH = new Function("tanh", 1);
    private static final Function ATANH = new Function("atanh", 1);
    
    private static final Function SQRT = new Function("sqrt", 1);
    private static final Function CBRT = new Function("cbrt", 1);
    private static final Function SQR = new Function("sqr", 1);
    private static final Function CUBE = new Function("cube", 1);
    private static final Function POW = new Function("pow", 1);
    private static final Function LOG = new Function("log", 1);
    private static final Function LN = new Function("ln", 1);
    private static final Function ABS = new Function("abs", 1);
    private static final Function FACT  = new Function("fact", 1);
    private static final Function PCT  = new Function("pct", 1);
    
    /** Defines the new constants.*/
    private static final Constant PI  = new Constant("π");
    private static final Constant E  = new Constant("℮");
    
    private static final Parameters PARAMS;
    
    private String toggleAngle;

    static {
        // Gets the default DoubleEvaluator's parameters
        PARAMS = DoubleEvaluator.getDefaultParameters();
        
        // add the new function to these parameters
        PARAMS.add(SIN);
        PARAMS.add(ASIN);
        PARAMS.add(COS);
        PARAMS.add(ACOS);
        PARAMS.add(TAN);
        PARAMS.add(ATAN);
        
        PARAMS.add(SINH);
        PARAMS.add(ASINH);
        PARAMS.add(COSH);
        PARAMS.add(ACOSH);
        PARAMS.add(TANH);
        PARAMS.add(ATANH);
        
        PARAMS.add(SQRT);
        PARAMS.add(CBRT);
        PARAMS.add(SQR);
        PARAMS.add(CUBE);
        PARAMS.add(POW);
        PARAMS.add(LOG);
        PARAMS.add(LN);
        PARAMS.add(ABS);
        PARAMS.add(FACT);
        PARAMS.add(PCT);
        
        // add the new constant to these parameters
        PARAMS.add(PI);
        PARAMS.add(E);
    }

    ExtendedDoubleEvaluator(String angle) {
        super(PARAMS);
        toggleAngle = angle;
    }

    @Override
    public Double evaluate(Function function, Iterator<Double> arguments, Object evaluationContext) {
        
        //Non-hyperbolic
        if (function == SIN) {
            // Implements the new function
            if (toggleAngle.equalsIgnoreCase("Rad")){
                return FastMath.sin(arguments.next());
                
            } else {
                return FastMath.sin(FastMath.toRadians(arguments.next()));
                
            }
        } else if (function == ASIN) {
            // Implements the new function
            if (toggleAngle.equalsIgnoreCase("Rad")){
                return FastMath.asin(arguments.next());
                
            } else {
                return FastMath.toDegrees(FastMath.asin(arguments.next()));
                
            }
        } else if (function == COS) {
            // Implements the new function
            if (toggleAngle.equalsIgnoreCase("Rad")){
                return FastMath.cos(arguments.next());
                
            } else {
                return FastMath.cos(FastMath.toRadians(arguments.next()));
                
            }
        } else if (function == ACOS) {
            // Implements the new function
            if (toggleAngle.equalsIgnoreCase("Rad")){
                return FastMath.acos(arguments.next());
                
            } else {
                return FastMath.toDegrees(FastMath.acos(arguments.next()));
                
            }
        } else if (function == TAN) {
            // Implements the new function
            if (toggleAngle.equalsIgnoreCase("Rad")){
                return FastMath.tan(arguments.next());
                
            } else {
                return FastMath.tan(FastMath.toRadians(arguments.next()));
                
            }
        } else if (function == ATAN) {
            // Implements the new function
            if (toggleAngle.equalsIgnoreCase("Rad")){
                return FastMath.atan(arguments.next());
                
            } else {
                return FastMath.toDegrees(FastMath.atan(arguments.next()));
                
            }
        } 
        
        
        //Hyperbolic
        else if (function == SINH) {
            // Implements the new function
            return FastMath.sinh(arguments.next());
            
        } else if (function == ASINH) {
            // Implements the new function
            return FastMath.asinh(arguments.next());
            
        } else if (function == COSH) {
            // Implements the new function
            return FastMath.cosh(arguments.next());
            
        } else if (function == ACOSH) {
            // Implements the new function
            return FastMath.acosh(arguments.next());
            
        } else if (function == TANH) {
            // Implements the new function
            return FastMath.tanh(arguments.next());
            
        } else if (function == ATANH) {
            // Implements the new function
            return FastMath.atanh(arguments.next());
            
        }
        
        
        //Other important functions.
        else if (function == SQRT){
            return FastMath.sqrt(arguments.next());
            
        } else if (function == CBRT){
            return FastMath.cbrt(arguments.next());
            
        } else if (function == SQR){
            return FastMath.pow(arguments.next(), 2);

        } else if (function == CUBE){
            return FastMath.pow(arguments.next(), 2);

        } else if (function == POW){
            return FastMath.pow(arguments.next(), arguments.next());
            
        } else if (function == LOG){
            return FastMath.log(arguments.next());
            
        } else if (function == LN){
            return FastMath.exp(arguments.next());
            
        } else if (function == ABS){
            return FastMath.abs(arguments.next());
            
        } else if (function == FACT){
            return FactorialClass.fact(arguments.next());
            
        } else if (function == PCT){
            return (arguments.next() / 100);

        }



        else{
            // If it's another function, pass it to DoubleEvaluator
            return super.evaluate(function, arguments, evaluationContext);
        }
    }
    
    
    @Override
    public Double evaluate(Constant constant, Object evaluationContext) {
        if (constant == PI){
            return FastMath.PI;
            
        } else if (constant == E){
            return FastMath.E;
        }
        
        
        
        else{
            return super.evaluate(constant, evaluationContext);
        }
    }
}
