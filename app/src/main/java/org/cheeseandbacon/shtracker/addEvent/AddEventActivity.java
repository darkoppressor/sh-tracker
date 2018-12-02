package org.cheeseandbacon.shtracker.addEvent;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.cheeseandbacon.shtracker.R;
import org.cheeseandbacon.shtracker.base.BaseActivity;
import org.cheeseandbacon.shtracker.data.event.Event;
import org.cheeseandbacon.shtracker.data.event.EventLoader;
import org.cheeseandbacon.shtracker.util.Vibration;

import java.util.ArrayList;
import java.util.UUID;

public class AddEventActivity extends BaseActivity {
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_TIME = "time";

    private TextView textTime;

    private String date;
    private String time;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(R.layout.activity_add_event, getString(R.string.add_event_title), null);

        textTime = findViewById(R.id.time);

        date = getIntent().getStringExtra(EXTRA_DATE);
        time = getIntent().getStringExtra(EXTRA_TIME);

        textTime.setText(time);
    }

    public void cancel (View view) {
        Vibration.buttonPress(this);

        finish();
    }

    public void done (View view) {
        Vibration.buttonPress(this);

        ArrayList<Event> data = new ArrayList<>();

        data.add(new Event(
                UUID.randomUUID().toString(),
                date,
                time
        ));

        EventLoader.load(this, eventDao -> eventDao.insert(Event.class, data, this::finish));
    }

    public void time (View view) {
        Vibration.buttonPress(this);

        ///QQQ allow time editing
    }
}
