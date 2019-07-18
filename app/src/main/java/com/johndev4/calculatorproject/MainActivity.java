package com.johndev4.calculatorproject;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.apache.commons.math3.util.FastMath;

import java.util.Objects;

import spencerstudios.com.jetdblib.JetDB;

/**
 * @author johndev4
 */

public class MainActivity extends AppCompatActivity {

    // Constant variable for error
    private static final String INVALID_FORMAT_USED = "Invalid format used.";
    private static final String CANNOT_DIVIDE_BY_ZERO = "Cannot divide to zero.";
    private static final String CALCULATION_OUTSIDE_THE_RANGE = "Calculation outside the range.";
    private static final String UNDEFINED_RESULT = "Undefined result.";

    // Constant variable of empty log
    private static final String EMPTY_LOG = "There's no history yet";

    // Constant variable for max length of display1
    private static final int MAX_DISPLAY_LENGTH = 15;


    // Variables for display
    private static String display1Text = "0";
    private static String display2Text = "";

    // Variable for computation of numbers
    private static double result = 0;

    private static String op = ""; //for: operators

    // Variable for geometry angle
    private static String angle = "Deg";

    // Variable for state of something..
    private static boolean invertFx = false;
    private static boolean autoMultiply = false;
    private static boolean negateAt0 = false;
    private static boolean operatorState = false;

    // Logs container
    private static String logs = EMPTY_LOG;

    // Clipboard Manager
    private ClipboardManager clipboardManager;

    // Display widget
    private EditText display1;
    private TextView display2;

    // Angle widget
    private TextView angleTextView;

    // ConstraintLayout
    private ConstraintLayout constraintLayout_logs;
    private ConstraintLayout constraintLayout_scientific_functions;
    private ConstraintLayout constraintLayout_numpad;
    private ConstraintLayout constraintLayout_tool;

    // Log TextView
    private TextView logsTextView;

    // Clear logs button
    private ImageButton btnClearLogs;

    // Scientific functions buttons
    private Button btnFunc1;
    private Button btnFunc2;
    private Button btnFunc3;
    private Button btnFunc4;
    private Button btnFunc5;
    private Button btnFunc6;
    private Button btnFunc7;
    private Button btnFunc8;
    private Button btnFunc9;
    private Button btnFunc10;
    private Button btnFunc11;
    private Button btnFunc12;
    private Button btnFunc13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        display1 = findViewById(R.id.display1EditText);
        display2 = findViewById(R.id.display2TextView);
        angleTextView = findViewById(R.id.angleTextView);
        constraintLayout_logs = findViewById(R.id.constraintLayout_logs);
        constraintLayout_numpad = findViewById(R.id.constraintLayout_numpad);
        constraintLayout_tool = findViewById(R.id.constraintLayout_tool);
        logsTextView = findViewById(R.id.logsTextView);
        btnClearLogs = findViewById(R.id.btnClearLogs);

        // Update Display 1& 2
        updateDisplay1();
        updateDisplay2();

        // set OnLongClick for display 1
        display1.setOnLongClickListener(longClickListener);

        // Disable btnClearLogs on empty history
        if (JetDB.getString(getApplicationContext(), "key_logs", EMPTY_LOG).equals(EMPTY_LOG)) {
            btnClearLogs.setEnabled(false);
        } else {
            btnClearLogs.setEnabled(true);
        }

        // This will prevents crash on app when changing from portrait to landscape
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            btnFunc1 = findViewById(R.id.btnFunc1);
            btnFunc2 = findViewById(R.id.btnFunc2);
            btnFunc3 = findViewById(R.id.btnFunc3);
            btnFunc4 = findViewById(R.id.btnFunc4);
            btnFunc5 = findViewById(R.id.btnFunc5);
            btnFunc6 = findViewById(R.id.btnFunc6);
            btnFunc7 = findViewById(R.id.btnFunc7);
            btnFunc8 = findViewById(R.id.btnFunc8);
            btnFunc9 = findViewById(R.id.btnFunc9);
            btnFunc10 = findViewById(R.id.btnFunc10);
            btnFunc11 = findViewById(R.id.btnFunc11);
            btnFunc12 = findViewById(R.id.btnFunc12);
            btnFunc13 = findViewById(R.id.btnFunc13);
            constraintLayout_scientific_functions = findViewById(R.id.constraintLayout_scientific_functions);

            // Update the text of scientific functions Button
            onClickInvertFx(findViewById(R.id.btnInvertFx));
            onClickInvertFx(findViewById(R.id.btnInvertFx));

            //Update the angle and angle TextView
            onClickToggleAngle(findViewById(R.id.btnToggleAngle));
            onClickToggleAngle(findViewById(R.id.btnToggleAngle));
        }
    }

    @Override
    public void onBackPressed() {
        if (constraintLayout_logs.getVisibility() != View.VISIBLE)
            super.onBackPressed();

        // This will prevents crash on app when changing from portrait to landscape
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintLayout_scientific_functions.setVisibility(View.VISIBLE);
        }

        constraintLayout_logs.setVisibility(View.GONE);
        constraintLayout_numpad.setVisibility(View.VISIBLE);
        constraintLayout_tool.setVisibility(View.VISIBLE);
    }

//-----------------------------------------------------------------------------------------------------------------------------------

    // On Click Open Logs
    public void onClickLogs(View view) {
        logs = JetDB.getString(getApplicationContext(), "key_logs", EMPTY_LOG);

        // This will prevents crash on app when changing from portrait to landscape
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintLayout_scientific_functions = findViewById(R.id.constraintLayout_scientific_functions);
            constraintLayout_scientific_functions.setVisibility(View.INVISIBLE);
        }

        constraintLayout_logs.setVisibility(View.VISIBLE);
        constraintLayout_numpad.setVisibility(View.INVISIBLE);
        constraintLayout_tool.setVisibility(View.INVISIBLE);
        logsTextView.setText(logs);
    }

    // On Click Clear logs
    public void onClickClearLogs(View view) {
        if (!logs.equals(EMPTY_LOG)) {
            JetDB.putString(getApplicationContext(), EMPTY_LOG, "key_logs");
            logs = JetDB.getString(getApplicationContext(), "key_logs", EMPTY_LOG);
            logsTextView.setText(logs);
            btnClearLogs.setEnabled(false);
        }
    }

    // On Click Copy
    public void onClickCopy(View view) {
        ClipData clipData = clipboardManager.hasPrimaryClip()? clipboardManager.getPrimaryClip() : null;

        if (clipData != null) {
            ClipData.Item item = clipData.getItemAt(0);

            if (!item.getText().toString().equals(display1.getText().toString())) {
                clipData = ClipData.newPlainText("textBoard", display1.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Copied to clipboard.", Toast.LENGTH_SHORT).show();

            } else{
                Toast.makeText(getApplicationContext(), "Item is already copied to clipboard.", Toast.LENGTH_SHORT).show();
            }

        } else{
            clipData = ClipData.newPlainText("textBoard", display1.getText().toString());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getApplicationContext(), "Copied to clipboard.", Toast.LENGTH_SHORT).show();
        }
    }

    // On Long Click Paste
    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View view) {
            if (clipboardManager.hasPrimaryClip()) {
                ClipData clipData = clipboardManager.getPrimaryClip();
                ClipData.Item item = Objects.requireNonNull(clipData).getItemAt(0);
                display1.setText(item.getText().toString());
            }

            return true;
        }
    };

    // On Click Rotate screen
    public void onClickRotate(View view) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Lock screen orientation on landscape
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Lock screen orientation on portrait
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    // On Click Clear all
    public void onClickClear(View view) {
        clearDisplay1();
        clearDisplay2();
        result = 0;
        op = "";
        autoMultiply = false;
        negateAt0 = false;
        operatorState = false;
    }

    // On Click Backspace
    public void onClickBackspace(View view) {
        negateAt0 = false;

        if (!display1Text.equals("0") && !operatorState && !display1Text.endsWith(")")) {
            display1Text = display1Text.substring(0, display1Text.length() - 1);

            if (display1Text.equals("-")) {
                display1Text = "0";
            }

            if (!display1Text.contains(".") || !display1Text.endsWith("0")) {
                if (!display1Text.endsWith(".")) {
                    display1Text = NumberClass.rebuild(display1Text, 15, 7);
                }
            }

            updateDisplay1();
        }
    }


    // On Click Operators including Exponent Operator (^)
    public void onClickOperator(View view) {
        Button b = (Button) view;

        String opNew = b.getText().toString().equals(getResources().getString(R.string.exponent_n)) ? "^" : " " + b.getText() + " ";
        autoMultiply = false;
        negateAt0 = false;

        display1Text = display1Text.endsWith(".") ? CharacterClass.remove('.', display1Text) : display1Text;

        if (display2Text.endsWith(")")) {
            result = ModifiedDoubleEvaluator.eval(display2Text, angle);
            display1Text = NumberClass.rebuild(result, 15, 7);
            updateDisplay1();
            op = opNew;
            display2Text += op;
            updateDisplay2();

        } else if (!operatorState || display2Text.isEmpty()) {
            if (display2Text.endsWith("^")) {
                display2Text += "(";
            }

            // Count for duplication of ')'.
            int cnt = CharacterClass.count('(', display2Text) - CharacterClass.count(')', display2Text);

            op = opNew;
            String expression = display2Text + display1Text + CharacterClass.duplicate(')', cnt) + op;
            try {
                // Assign evaluated expression to result variable
                result = ModifiedDoubleEvaluator.eval(expression, angle);
            } catch (Exception e) {
                // Toast information if the evaluated expression is not valid
                Toast.makeText(getApplicationContext(), INVALID_FORMAT_USED, Toast.LENGTH_SHORT).show();
                return;
            }

            //Try if the divisor is zero
            if (expression.endsWith(" ÷ 0" + op)) {
                Toast.makeText(getApplicationContext(), CANNOT_DIVIDE_BY_ZERO, Toast.LENGTH_SHORT).show();
                return;
            }
            // Try if the result is infinite.
            if (Double.isInfinite(result)) {
                Toast.makeText(getApplicationContext(), CALCULATION_OUTSIDE_THE_RANGE, Toast.LENGTH_SHORT).show();
                return;
            }
            // Try if the result is NaN
            if (Double.isNaN(result)) {
                Toast.makeText(getApplicationContext(), UNDEFINED_RESULT, Toast.LENGTH_SHORT).show();
                return;
            }

            display2Text = expression;
            updateDisplay2();
            display1Text = NumberClass.rebuild(result, 15, 7);
            updateDisplay1();

        } else {
            String opOld = op;
            op = opNew;
            display2Text = CharacterClass.replaceTarget(display2Text, opOld + "@last", op);
            updateDisplay2();
            result = ModifiedDoubleEvaluator.eval(display2Text, angle);
            display1Text = NumberClass.rebuild(result, 15, 7);
            updateDisplay1();
        }

        operatorState = true;
    }


    // On Click Equals
    public void onClickEqual(View view) {
        display1Text = display1Text.endsWith(".") ? CharacterClass.remove('.', display1Text) : display1Text;
        updateDisplay1();

        if (display2Text.endsWith(")")) {
            result = ModifiedDoubleEvaluator.eval(display2Text, angle);
            display1Text = NumberClass.rebuild(result, 15, 7);
            updateDisplay1();

        } else if (!display2Text.isEmpty()) {
            // Count for duplication of ')'.
            int cnt = CharacterClass.count('(', display2Text) - CharacterClass.count(')', display2Text);

            op = "";
            String expression = display2Text + display1Text + CharacterClass.duplicate(')', cnt) + op;
            try {
                // Assign evaluated expression to result variable
                result = ModifiedDoubleEvaluator.eval(expression, angle);
            } catch (Exception e) {
                // Toast information if the evaluated expression is not valid
                Toast.makeText(getApplicationContext(), INVALID_FORMAT_USED, Toast.LENGTH_SHORT).show();
                return;
            }

            //Try if the divisor is zero
            if (expression.endsWith(" ÷ 0")) {
                Toast.makeText(getApplicationContext(), CANNOT_DIVIDE_BY_ZERO, Toast.LENGTH_SHORT).show();
                return;
            }
            // Try if the result is infinite.
            if (Double.isInfinite(result)) {
                Toast.makeText(getApplicationContext(), CALCULATION_OUTSIDE_THE_RANGE, Toast.LENGTH_SHORT).show();
                return;
            }
            // Try if the result is NaN
            if (Double.isNaN(result)) {
                Toast.makeText(getApplicationContext(), UNDEFINED_RESULT, Toast.LENGTH_SHORT).show();
                return;
            }

            display2Text = expression;
            display1Text = NumberClass.rebuild(result, 15, 7);
            updateDisplay1();
        }

        // Save expression to log
        if (!display2Text.isEmpty()) {
            logs = JetDB.getString(getApplicationContext(), "key_logs", EMPTY_LOG);

            if (logs.equals(EMPTY_LOG))
                logs = "";

            logs = logs + display2Text + "\n = " + display1Text + "\n\n";
            JetDB.putString(getApplicationContext(), logs, "key_logs");
            btnClearLogs.setEnabled(true);
        }

        clearDisplay2();
        operatorState = true;
    }


    // On Click Numbers
    public void onClickNumber(View view) {
        Button b = (Button) view;

        if (autoMultiply) {
            onClickOperator(findViewById(R.id.btnMultiply));
        }

        if (operatorState) {
            display1Text = "0";
            operatorState = false;
        }

        if (NumberClass.length(display1Text) < MAX_DISPLAY_LENGTH) {
            if (!display1Text.equals("0")) {
                display1Text += b.getText();

            } else {
                display1Text = "";
                display1Text += b.getText();
            }

            if (!display1Text.contains(".") || !display1Text.endsWith("0")) {
                display1Text = NumberClass.rebuild(display1Text, 15, 7);
            }

            updateDisplay1();

            if (negateAt0 && !b.getText().equals("0")) {
                onClickNegate(findViewById(R.id.btnNegate));
            }
        } else {
            Toast.makeText(getApplicationContext(), "Can't enter more than " + MAX_DISPLAY_LENGTH + " digit.", Toast.LENGTH_SHORT).show();
        }
    }

    // On Click Period
    public void onClickPeriod(View view) {
        if (NumberClass.length(display1Text) < MAX_DISPLAY_LENGTH) {
            if (autoMultiply) {
                onClickOperator(findViewById(R.id.btnMultiply));
            }

            if (operatorState) {
                display1Text = "0";
                operatorState = false;
            }

            if (!display1Text.contains(".")) {
                if (display1Text.isEmpty()) {
                    display1Text = "0";
                }
                display1Text += ".";
            }
            updateDisplay1();
        }
    }

    // On Click Negative Sign
    public void onClickNegate(View view) {
        if (NumberClass.length(display1Text) <= MAX_DISPLAY_LENGTH || display1Text.startsWith("-")) {
            if (operatorState) {
                display1Text = "0";
                operatorState = false;
            }

            if (display1Text.endsWith(")")) {
                String temp = display1Text;

                String expression;
                if (!display1Text.startsWith("-")) {
                    expression = "-" + display1Text;
                } else {
                    expression = display1Text.substring(1);
                }

                try {
                    // Assign evaluated expression to result variable
                    result = ModifiedDoubleEvaluator.eval(expression, angle);
                } catch (Exception e) {
                    // Toast information if the evaluated expression is not valid
                    Toast.makeText(getApplicationContext(), INVALID_FORMAT_USED, Toast.LENGTH_SHORT).show();
                    return;
                }

                display2Text = CharacterClass.replaceTarget(display2Text, temp + "@last", expression);
                updateDisplay2();
                display1Text = NumberClass.rebuild(result, 15, 7);
                updateDisplay1();
                display1Text = expression;
            } else if (!display1Text.equals("0") && !display1Text.equals("0.") && !display1Text.isEmpty()) {
                if (display1Text.contains("-")) {
                    display1Text = display1Text.substring(1);
                } else {
                    display1Text = "-" + display1Text;
                }

                updateDisplay1();
                negateAt0 = false;

            } else {
                negateAt0 = !negateAt0;
            }
        }
    }

    // On Click Invert Scientific Functions
    public void onClickInvertFx(View view) {
        if (invertFx) {
            btnFunc1.setText(R.string.base_10);
            btnFunc2.setText(R.string.sin);
            btnFunc3.setText(R.string.cos);
            btnFunc4.setText(R.string.tan);
            btnFunc5.setText(R.string.sinh);
            btnFunc6.setText(R.string.cosh);
            btnFunc7.setText(R.string.tanh);
            btnFunc8.setText(R.string.square_root);
            btnFunc9.setText(R.string.squared);
            btnFunc10.setText(R.string.exponent_n);
            btnFunc11.setText(R.string.E);
            btnFunc12.setText(R.string.Pi);
            btnFunc13.setText(R.string.log10);

        } else {
            btnFunc1.setText(R.string.base_2);
            btnFunc2.setText(R.string.asin);
            btnFunc3.setText(R.string.acos);
            btnFunc4.setText(R.string.atan);
            btnFunc5.setText(R.string.asinh);
            btnFunc6.setText(R.string.acosh);
            btnFunc7.setText(R.string.atanh);
            btnFunc8.setText(R.string.cube_root);
            btnFunc9.setText(R.string.cubed);
            btnFunc10.setText(R.string.absolute);
            btnFunc11.setText(R.string.base_E);
            btnFunc12.setText(R.string.factorial);
            btnFunc13.setText(R.string.ln);
        }

        // change invertFx state
        invertFx = !invertFx;
    }

    // On Click Toggle Angle
    public void onClickToggleAngle(View view) {
        Button b = (Button) view;

        if (angle.equals("Rad")) {
            b.setText(R.string.rad);
            angle = "Deg";
            angleTextView.setText(angle);
            angleTextView.setVisibility(View.INVISIBLE);
        } else if (angle.equals("Deg")) {
            b.setText(R.string.deg);
            angle = "Rad";
            angleTextView.setText(angle);
            angleTextView.setVisibility(View.VISIBLE);
        }
    }

    // On Click Percentage and Scientific Function
    public void onClickFunc(View view) {
        Button b = (Button) view;
        String function = "";

        // Percentage
        if (b.getText().toString().equals(getResources().getString(R.string.percent))) {
            function = "pct";

            // Scientific Function
        } else if (b.getText().toString().equals(getResources().getString(R.string.base_10))) {
            function = "10^";

        } else if (b.getText().toString().equals(getResources().getString(R.string.base_2))) {
            function = "2^";

        } else if (b.getText().toString().equals(getResources().getString(R.string.sin))) {
            function = "sin";

        } else if (b.getText().toString().equals(getResources().getString(R.string.asin))) {
            function = "asin";

        } else if (b.getText().toString().equals(getResources().getString(R.string.cos))) {
            function = "cos";

        } else if (b.getText().toString().equals(getResources().getString(R.string.acos))) {
            function = "acos";

        } else if (b.getText().toString().equals(getResources().getString(R.string.tan))) {
            function = "tan";

        } else if (b.getText().toString().equals(getResources().getString(R.string.atan))) {
            function = "atan";

        } else if (b.getText().toString().equals(getResources().getString(R.string.sinh))) {
            function = "sinh";

        } else if (b.getText().toString().equals(getResources().getString(R.string.asinh))) {
            function = "asinh";

        } else if (b.getText().toString().equals(getResources().getString(R.string.cosh))) {
            function = "cosh";

        } else if (b.getText().toString().equals(getResources().getString(R.string.acosh))) {
            function = "acosh";

        } else if (b.getText().toString().equals(getResources().getString(R.string.tanh))) {
            function = "tanh";

        } else if (b.getText().toString().equals(getResources().getString(R.string.atanh))) {
            function = "atanh";

        } else if (b.getText().toString().equals(getResources().getString(R.string.square_root))) {
            function = "sqrt";

        } else if (b.getText().toString().equals(getResources().getString(R.string.cube_root))) {
            function = "cbrt";

        } else if (b.getText().toString().equals(getResources().getString(R.string.squared))) {
            function = "sqr";

        } else if (b.getText().toString().equals(getResources().getString(R.string.cubed))) {
            function = "cube";

        } else if (b.getText().toString().equals(getResources().getString(R.string.absolute))) {
            function = "abs";

        } else if (b.getText().toString().equals(getResources().getString(R.string.E))) {
            function = "℮";

        } else if (b.getText().toString().equals(getResources().getString(R.string.base_E))) {
            function = "℮^";

        } else if (b.getText().toString().equals(getResources().getString(R.string.Pi))) {
            function = "π";

        } else if (b.getText().toString().equals(getResources().getString(R.string.factorial))) {
            function = "fact";

        } else if (b.getText().toString().equals(getResources().getString(R.string.log10))) {
            function = "log";

        } else if (b.getText().toString().equals(getResources().getString(R.string.ln))) {
            function = "ln";
        }

        if (!function.equals("℮") && !function.equals("π")) {
            String temp = display1Text;
            String expression = function + "(" + display1Text + ")";

            try {
                // Assign evaluated expression to result variable
                result = ModifiedDoubleEvaluator.eval(expression, angle);
            } catch (Exception e) {
                // Toast information if the evaluated expression is not valid
                Toast.makeText(getApplicationContext(), INVALID_FORMAT_USED, Toast.LENGTH_SHORT).show();
                return;
            }

            // Try if the result is infinite.
            if (Double.isInfinite(result)) {
                Toast.makeText(getApplicationContext(), CALCULATION_OUTSIDE_THE_RANGE, Toast.LENGTH_SHORT).show();
                return;
            }
            // Try if the result is NaN
            if (Double.isNaN(result)) {
                Toast.makeText(getApplicationContext(), UNDEFINED_RESULT, Toast.LENGTH_SHORT).show();
                return;
            }

            display2Text = !display2Text.endsWith(temp) ? display2Text + expression : CharacterClass.replaceTarget(display2Text, temp + "@last", expression);
            updateDisplay2();
            display1Text = NumberClass.rebuild(result, 15, 7);
            updateDisplay1();
            display1Text = expression;

        } else {
            if (!display1Text.equals("0")) {
                onClickOperator(findViewById(R.id.btnMultiply));
            }

            if (display1Text.equals("0") || display1Text.isEmpty() || !display1Text.equals(FastMath.PI + "")) {
                display1Text = function.equals("℮") ? FastMath.E + "" : FastMath.PI + "";
                updateDisplay1();
            }
        }

        autoMultiply = true;
    }


//-----------------------------------------------------------------------------------------------------------------------------------
    /* Private methods for updating the text of views
     */

    private void updateDisplay1() {
        // Set text for display1
        display1.setText(display1Text);
        // add TextChangedListener
        display1.addTextChangedListener(textWatcher);
    }

    private void updateDisplay2() {
        //Set text for display2
        display2.setText(display2Text);
    }

    private void clearDisplay1() {
        // Reset to zero
        display1Text = "0";
        updateDisplay1();
    }

    private void clearDisplay2() {
        // Reset to empty
        display2Text = "";
        updateDisplay2();
    }

    // This will update other variable if display1 text changed.
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            /* It is for the reflection of display1EditText to its variable display1Text when it is
             * empty or contains scientific expression (Eg. sqrt(5));
             */
            if (display1 != null && !display1Text.endsWith(")")) {
                display1Text = display1.getText().toString();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}



/* Start Date: May 1, 2019 Time 3:00pm
 * End Date: July 2,2019 Time 12:47pm
 */