package com.fisei.athanasiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fisei.athanasiaapp.models.ResponseAthanasia;
import com.fisei.athanasiaapp.objects.UserClient;
import com.fisei.athanasiaapp.services.UserClientService;

import org.json.JSONObject;

import java.net.URL;

public class SingUpActivity_SSNT extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextCedula;
    private EditText editTextPassword;
    private TextView errorTextView;
    private Button buttonSignUp;
    private ResponseAthanasia responseTask = new ResponseAthanasia(false, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        InitializeViewComponents();



    }
    private class SignUpTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            UserClient newUser = new UserClient(0, editTextName.getText().toString(),
                    editTextEmail.getText().toString() + "@ath.com",
                    editTextCedula.getText().toString(), "");

            responseTask = UserClientService.SignUpNewUser(newUser, editTextPassword.getText().toString());
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            if(responseTask.Success){
                StartLoginActivity();
            } else {
                errorTextView.setText(responseTask.Message);
            }
            responseTask.Success = false;
        }
    }
    private void InitializeViewComponents(){
        editTextEmail = (EditText) findViewById(R.id.editTextSignUpEmail);
        editTextName = (EditText) findViewById(R.id.editTextSignUpName);
        editTextCedula = (EditText) findViewById(R.id.editTextSignUpCedula);
        editTextPassword = (EditText) findViewById(R.id.editTextSignUpPassword);
        errorTextView = (TextView) findViewById(R.id.textViewSignUpFail1);
        buttonSignUp = (Button) findViewById(R.id.btnSignUp);
        buttonSignUp.setOnClickListener(signUpButtonClicked);
    }
    private void SignUp(){
        if(editTextEmail.getText().toString().isEmpty() || editTextName.getText().toString().isEmpty() ||
                editTextCedula.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){
            errorTextView.setText(R.string.fields_empty_error);

        } else {
            if(editTextPassword.getText().toString().length()<6 || editTextPassword.getText().toString().length()>10){
                errorTextView.setText("la contrasena debe tener mas de 6 caracteres y menos que 10");
            }else{
                boolean s=editTextPassword.getText().toString().matches(".*[[:punct:]]");
                if(!editTextPassword.getText().toString().matches(".*[[:punct:]]")){
                    errorTextView.setText("la contrasena debe tener un caracter especial");
                }else{
                    if(!editTextPassword.getText().toString().matches(".*\\d.*")){
                        errorTextView.setText("la contrasena debe tener numeros");
                    }else{
                        if(!editTextPassword.getText().toString().matches(".*[a-z].*")){
                            errorTextView.setText("la contrasena debe tener minusculas");
                        }else{
                            if(!editTextPassword.getText().toString().matches(".*[A-Z].*")){
                                errorTextView.setText("la contrasena debe tener mayusculas");
                            }else{
                                errorTextView.setText("");
                                SignUpTask signUpTask = new SignUpTask();
                                signUpTask.execute();
                            }
                        }
                    }
                }
            }


        }
    }
    private void StartLoginActivity(){
        Intent backLogin = new Intent(this, LoginActivity_SSNT.class);
        startActivity(backLogin);
        Toast.makeText(this, "Your register was successful", Toast.LENGTH_SHORT).show();
    }
    private final View.OnClickListener signUpButtonClicked = view -> SignUp();

}