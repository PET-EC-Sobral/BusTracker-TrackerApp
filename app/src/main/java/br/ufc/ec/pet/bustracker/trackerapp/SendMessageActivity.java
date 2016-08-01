package br.ufc.ec.pet.bustracker.trackerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.ufc.ec.pet.bustracker.trackerapp.types.Message;

public class SendMessageActivity extends AppCompatActivity {
    EditText mTitleEd, mMessageEd;
    Button mSendMessageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message_activity);

        mTitleEd = (EditText) findViewById(R.id.title_message_ed);
        mMessageEd = (EditText) findViewById(R.id.message_ed);
        mSendMessageBtn = (Button) findViewById(R.id.send_message_btn);

        setEvents();
    }
    private void setEvents(){
        final AppCompatActivity activity = this;
        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(mTitleEd.getText().toString(), mMessageEd.getText().toString());

                Intent it = new Intent(v.getContext(), TrackerService.class);
                it.putExtra("MESSAGE", message);
                startService(it);
                activity.finish();
            }
        });
    }
}
