package br.ufc.ec.pet.bustracker.trackerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.i2p.android.ext.floatingactionbutton.FloatingActionButton;
import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.ufc.ec.pet.bustracker.trackerapp.types.Bus;
import br.ufc.ec.pet.bustracker.trackerapp.types.Route;

public class ConnectActivity extends AppCompatActivity {
    private EditText mHostEt, mIdRouteEt, mNameRouteEt, mDescriptionRouteEt,
            mIdBusEt, mTimeIntervalEt, mDistanceMin;
    private ToggleButton mStartBtn;
    private Bus mBus;
    private Route mRoute;
    private ConnectionManager mConnectionManager;
    private Handler mUpdateLogHandler;
    private FloatingActionButton mSendMessageFab;
    private FloatingActionsMenu mActionFam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mConnectionManager = new ConnectionManager(this, "");
        Intent it = getIntent();
        if(it != null ) {
            if (it.hasExtra("TOKEN")) {
                mConnectionManager.setToken(it.getStringExtra("TOKEN"));
                Log.d("Bus", mConnectionManager.getToken());
            }
            if(it.hasExtra("HOST")){
                mConnectionManager.setServerPrefix(it.getStringExtra("HOST"));
            }
        }


        mHostEt = (EditText) findViewById(R.id.host_et);
        mHostEt.setEnabled(false);
        mHostEt.setText(mConnectionManager.getServerPrefix());
        mIdRouteEt = (EditText) findViewById(R.id.id_route_et);
        mNameRouteEt = (EditText) findViewById(R.id.name_route_et);
        mDescriptionRouteEt = (EditText) findViewById(R.id.description_route_et);
        mIdBusEt = (EditText) findViewById(R.id.id_bus_et);
        mTimeIntervalEt = (EditText) findViewById(R.id.time_interval_et);
        mDistanceMin = (EditText) findViewById(R.id.radius_position_et);

        mStartBtn = (ToggleButton) findViewById(R.id.start_btn);

        mBus = new Bus();
        mRoute = new Route();

        LogFile.writeln(this, "Sessão iniciada: "+(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())));


        mSendMessageFab = (FloatingActionButton) findViewById(R.id.send_message_fab);
        mActionFam = (FloatingActionsMenu) findViewById(R.id.actions_fam);

        setEvents();
    }
    private void saveInputs(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("BUS_ID", mIdBusEt.getText().toString());
        editor.putString("ROUTE_ID", mIdRouteEt.getText().toString());
        editor.putString("ROUTE_NAME", mNameRouteEt.getText().toString());
        editor.putString("ROUTE_DESCRIPTION", mDescriptionRouteEt.getText().toString());
        editor.putString("TIME_INTERVAL", mTimeIntervalEt.getText().toString());
        editor.putString("DISTANCE_MIN", mDistanceMin.getText().toString());
        editor.commit();
    }
    private void rememberInputs(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        mIdBusEt.setText(sharedPref.getString("BUS_ID", ""));
        mIdRouteEt.setText(sharedPref.getString("ROUTE_ID", ""));
        mHostEt.setText(mConnectionManager.getServerPrefix());
        mNameRouteEt.setText(sharedPref.getString("ROUTE_NAME", ""));
        mDescriptionRouteEt.setText(sharedPref.getString("ROUTE_DESCRIPTION", ""));
        mTimeIntervalEt.setText(sharedPref.getString("TIME_INTERVAL", "1000"));
        mDistanceMin.setText(sharedPref.getString("DISTANCE_MIN", "10"));
    }
    private void setEvents(){
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Bus", "clicked");

                if(mStartBtn.isChecked()) {
                    mConnectionManager.setServerPrefix(getHost());
                    mConnectionManager.putRoute(getRoute());

                    Intent intent = new Intent(v.getContext(), TrackerService.class);
                    intent.putExtra("BUS", getBus());
                    intent.putExtra("ROUTE", getRoute());
                    intent.putExtra("HOST", getHost());
                    intent.putExtra("TIME_INTERVAL", getTimeInterval());
                    intent.putExtra("TOKEN", mConnectionManager.getToken());
                    intent.putExtra("DISTANCE_MIN", getDistanceMin());
                    startService(intent);
                    mActionFam.setVisibility(View.VISIBLE);
                }
                else {
                    mActionFam.setVisibility(View.INVISIBLE);
                    stopService(new Intent(v.getContext(), TrackerService.class));
                }
            }
        });

        mSendMessageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(v.getContext(), SendMessageActivity.class);
                startActivity(it);
                mActionFam.collapse();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mActionFam.isExpanded())
            mActionFam.collapse();
        else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        rememberInputs();

        if(TrackerService.isRunning()) {
            mStartBtn.setChecked(true);
            mActionFam.setVisibility(View.VISIBLE);
            LogFile.writeln(this, "O serviço está ligado.", true);
        }
        else {
            mStartBtn.setChecked(false);
            mActionFam.setVisibility(View.INVISIBLE);
            LogFile.writeln(this, "O serviço está desligado.", true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInputs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connect_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_log:
                Intent it = new Intent(this, LogcatView.class);
                startActivity(it);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private Bus getBus(){
        mBus.setId(Integer.parseInt(mIdBusEt.getText().toString()));
        return mBus;
    }
    private Route getRoute(){
        mRoute.setId(Integer.parseInt(mIdRouteEt.getText().toString()));
        mRoute.setDescription(mDescriptionRouteEt.getText().toString());
        mRoute.setName(mNameRouteEt.getText().toString());
        return mRoute;
    }
    private String getHost(){
        return mHostEt.getText().toString();
    }
    private long getTimeInterval(){
        return Long.parseLong(mTimeIntervalEt.getText().toString());
    }
    private float getDistanceMin(){
        return Float.parseFloat(mDistanceMin.getText().toString());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
