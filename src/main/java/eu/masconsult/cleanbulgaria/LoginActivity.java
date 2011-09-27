package eu.masconsult.cleanbulgaria;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.inject.Inject;
import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.ConnectionException;
import eu.masconsult.cleanbulgaria.connection.InvalidDataException;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class LoginActivity extends RoboActivity {

    @InjectView(R.id.emailText)
    EditText emailTextEdit;

    @InjectView(R.id.passwordText)
    EditText passwordTextEdit;

    @InjectView(R.id.loginButton)
    Button loginButton;

    @Inject
    Connection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        emailTextEdit.setText("dani7@abv.bg");
        passwordTextEdit.setText("alabala");


        passwordTextEdit.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == EditorInfo.IME_ACTION_DONE || event.getAction() == EditorInfo.IME_NULL) {
                    loginButton.performClick();
                    return true;
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = emailTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
                if (email.equals("") || password.equals("")) {
                    Toast validationToast = Toast.makeText(getApplicationContext(), "Попълнете Полетата", 2);
                    validationToast.show();
                    return;
                }
                Context context = getApplicationContext();
                ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, context.getString(R.string.loginProcessTitle), context.getString(R.string.loginProcessMessage));
                new LoginTask(getApplicationContext(), progressDialog).execute(email, password);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, Integer> {

        private Context context;
        private ProgressDialog progressDialog;

        private static final int LOGIN_SUCCESS = 1;
        private static final int INVALID_CREDENTIALS = 2;
        private static final int NO_CONNECTION = 3;

        public LoginTask(Context context, ProgressDialog progressDialog) {
            this.context = context;
            this.progressDialog = progressDialog;
        }

        @Override
        protected Integer doInBackground(String... userCredentials) {
            try {
                connection.login(userCredentials[0], userCredentials[1]);
            } catch (InvalidDataException e) {
                return INVALID_CREDENTIALS;
            } catch (ConnectionException e) {
                return NO_CONNECTION;
            }
            return LOGIN_SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            switch (result) {
                case LOGIN_SUCCESS:
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    break;

                case INVALID_CREDENTIALS:
                    Toast.makeText(context, context.getString(R.string.invalidCredentials), Toast.LENGTH_LONG).show();
                    break;

                case NO_CONNECTION:
                    Toast.makeText(context, context.getString(R.string.noConnection), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
