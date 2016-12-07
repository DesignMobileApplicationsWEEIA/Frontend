package adm.virtualcampuswalk;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new GameFragmentsActivity(getSupportFragmentManager(), getResources().getConfiguration());
        pager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
