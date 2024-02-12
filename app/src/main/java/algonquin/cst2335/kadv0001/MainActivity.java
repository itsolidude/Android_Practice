package algonquin.cst2335.kadv0001;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity for a password checker application.
 * This activity allows the user to enter a password and it then checks its complexity.
 * Note: JavaDocs is still broken.
 *
 * @author Oliver K. - 041096826
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** TextView to show the password prompt or result */
    private TextView textView;

    /** EditText for password input */
    private EditText passwordInput;

    /** Button to trigger the login action */
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.ViewPasswordPrompt);
        passwordInput = findViewById(R.id.textInputPassword);
        loginButton = findViewById(R.id.LoginButton);

        loginButton.setOnClickListener( v -> {
            String password = passwordInput.getText().toString();
            boolean isComplex = checkPasswordComplexity(password);
            if (isComplex) {
                textView.setText("Your password meets the requirements");
            } else {
                textView.setText("You shall not pass!");
            }
        });
    }

    /** This function aims to check if this string has an Upper Case letter,
     *  a lower case letter, a number, and a special symbol (#$%^&*!@?)
     *
     * @param password The String object that we are checking
     * @return Returns TRUE if password meets standards and FALSE if it doesn't.
     */
    private boolean checkPasswordComplexity(String password) {
        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumber = false;
        boolean foundSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) foundUpperCase = true;
            if (Character.isLowerCase(c)) foundLowerCase = true;
            if (Character.isDigit(c)) foundNumber = true;
            if (isSpecialCharacter(c)) foundSpecial = true;
        }

        if (!foundUpperCase) {
            Toast.makeText(this, "Your password does not have an upper case letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(this, "Your password does not have a lower case letter", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundNumber) {
            Toast.makeText(this, "Your password does not have a number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(this, "Your password does not have a special symbol", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All conditions are met
    }

    /**
     * Checks if the given character is a special character.
     *
     * @param c the character to check
     * @return true if the character is a special character, false otherwise
     */
    private boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }

}