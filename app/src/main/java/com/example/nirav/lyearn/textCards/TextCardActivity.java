package com.example.nirav.lyearn.textCards;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.ActivityChooserModel;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nirav.lyearn.HomeCardAdapter;
import com.example.nirav.lyearn.HomeCards;
import com.example.nirav.lyearn.MainApplication;
import com.example.nirav.lyearn.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TextCardActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private TextCardAdapter mCardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_card);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(MainApplication.getAppContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) findViewById(R.id.card_home);

        mCardAdapter = new TextCardAdapter(getData());

        mRecyclerView.setAdapter(mCardAdapter);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<TextCards> getData() {
        ArrayList<TextCards> data  = new ArrayList<>();

        String[] titles = {"Test1", "Test2", "fdsafasd", "fdsfas","fdsfas"};
        String[] description = {"24133-24133/com.example.nirav.lyearn I/ViewUtils", "No adapter attached; skipping layout", "fsdfasfdsa", "fdsfas", "fdsfsadfsadfsadfasdfasd"};

        for (int i = 0; i<titles.length && i<description.length; i++ ) {

            TextCards current = new TextCards(titles[i], description[i]);
            data.add(current);
        }

        return data;
    }
}
