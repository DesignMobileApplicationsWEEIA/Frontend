package adm.virtualcampuswalk;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import adm.virtualcampuswalk.fragments.game.GameAchievementsFragment;
import adm.virtualcampuswalk.fragments.game.GameCameraViewFragment;
import adm.virtualcampuswalk.fragments.game.GameMapViewFragment;

/**
 * Created by Adam Piech on 2016-10-20.
 */

public class GameFragmentsActivity extends FragmentStatePagerAdapter {

    private Configuration conf;
    private int numberOfFragments = 3;
    private String fragmentTitle[] = {"Camera Game Mode", "Map Game Mode", "Achievements"};

    public GameFragmentsActivity(FragmentManager fragmentManager, Configuration conf) {
        super(fragmentManager);
        this.conf = conf;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GameCameraViewFragment();
            case 1:
                return new GameMapViewFragment();
            case 2:
                return new GameAchievementsFragment();
        }
        return null;
    }

    @Override
    public float getPageWidth(int position) {
        float nbPages;
        if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            nbPages = 1;
        } else {
            nbPages = 1;
        }
        return (1 / nbPages);
    }

    @Override
    public int getCount() {
        return numberOfFragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle[position];
    }
}
