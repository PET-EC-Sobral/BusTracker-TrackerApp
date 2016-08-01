package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity implements UserConnectionManagerListener {
    UserConnectionManager mConnection;
    EditText mEmailEt, mPasswordEt;
    Button mSignInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mConnection = new UserConnectionManager(this, getResources().getString(R.string.host_default));
        //UserConnectionManager.User user = new UserConnectionManager.User("asda","sadas");

        mEmailEt = (EditText) findViewById(R.id.email_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mSignInBtn = (Button) findViewById(R.id.signin_btn);

        setEvents();
    }
    private void setEvents(){
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEt.getText().toString();
                String password = mPasswordEt.getText().toString();
                UserConnectionManager.User user = mConnection.getUser(email, password);
                mConnection.login(user);
            }
        });
        mConnection.setUserConnectionManagerListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPasswordEt.setText("");
    }

    @Override
    public void onLogin(UserConnectionManager connection) {
        if(connection.hasToken()){
            Log.d("Bus", "inside");
            Intent it = new Intent(this, ConnectActivity.class);
            String token = mConnection.getToken();
            Log.d("Bus", "token: "+mConnection.getToken());
            it.putExtra("TOKEN", token);
            startActivity(it);
        }
    }
}
