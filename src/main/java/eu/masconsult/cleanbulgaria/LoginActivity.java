package eu.masconsult.cleanbulgaria;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.inject.Inject;
import eu.masconsult.cleanbulgaria.connection.Connection;
import eu.masconsult.cleanbulgaria.connection.ConnectionException;
import eu.masconsult.cleanbulgaria.connection.InvalidDataException;
import roboguice.activity.RoboActivity;
import roboguice.event.Observes;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

public class LoginActivity extends RoboActivity {

    @InjectView(R.id.emailText)
    EditText emailTextEdit;

    @InjectView(R.id.passwordText)
    EditText passwordTextEdit;

    @InjectView(R.id.loginButton)
    Button loginButton;

    @Inject
    Connection connection;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        emailTextEdit.setText("dani7@abv.bg");
        passwordTextEdit.setText("alabala");

        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(R.string.loginProcessTitle);
        progressDialog.setMessage(getString(R.string.loginProcessMessage));

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
                progressDialog.show();
                new LoginTask(getApplicationContext()).execute(email, password);
            }
        });
    }

//    private class RoboLoginTask extends RoboAsyncTask<String> {
//
//        private String email;
//        private String password;
//
//        public RoboLoginTask(String email, String password) {
//            this.email = email;
//            this.password = password;
//        }
//
//        @Override
//        public String call() throws Exception {
//            connection.login(email, password);
//            return "";
//        }
//
//        @Override
//        protected void onSuccess(String result) throws Exception {
//            progressDialog.dismiss();
//            Intent intent = new Intent(context, MainActivity.class);
//            startActivity(intent);
//        }
//
//        @Override
//        protected void onException(Exception e) throws RuntimeException {
//            progressDialog.dismiss();
//            if(e.getClass().equals(InvalidDataException.class)) {
//                Toast.makeText(context, context.getString(R.string.invalidCredentials), Toast.LENGTH_LONG).show();
//            } else if(e.getClass().equals(ConnectionException.class)) {
//                Toast.makeText(context, context.getString(R.string.noConnection), Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private class LoginTask extends AsyncTask<String, Void, Integer> {

        private Context context;

        private static final int LOGIN_SUCCESS = 1;
        private static final int INVALID_CREDENTIALS = 2;
        private static final int NO_CONNECTION = 3;

        public LoginTask(Context context) {
            this.context = context;
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
