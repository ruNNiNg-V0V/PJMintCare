package com.mintcare.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import com.mintcare.R;
import com.mintcare.alarmclock.activities.*;
import com.mintcare.riontech.*;

public class MainActivity extends AppCompatActivity {

    Date currentTime = Calendar.getInstance().getTime();

    TextView thisTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisTime = (TextView) findViewById(R.id.thisTime);

        thisTime.setText(currentTime.toString());

        Button btnAlarm = (Button) findViewById(R.id.btnAlarm);
        Button btnCusCal = (Button) findViewById(R.id.btnCalendar);

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getAlarm = new Intent(view.getContext(), AlarmPreview.class);
                view.getContext().startActivity(getAlarm);
            }
        });//알람 인텐트*/

        btnCusCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getCusCal = new Intent(view.getContext(), CusCalActivity.class);
                view.getContext().startActivity(getCusCal);
            }
        });//캘린더 인텐트


    }
}
