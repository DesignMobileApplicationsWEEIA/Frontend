package adm.virtualcampuswalk;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        viewPager = (ViewPager) findViewById(R.id.game_pager);
        PagerAdapter pagerAdapter = new GameFragmentActivity(getSupportFragmentManager(), getResources().getConfiguration());
        viewPager.setAdapter(pagerAdapter);
    }

}
