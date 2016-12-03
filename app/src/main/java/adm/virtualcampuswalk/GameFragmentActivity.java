package adm.virtualcampuswalk;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import adm.virtualcampuswalk.fragments.game.AchievementViewFragment;
import adm.virtualcampuswalk.fragments.game.CameraViewFragment;
import adm.virtualcampuswalk.fragments.game.MapViewFragment;

/**
 * Created by mariusz on 03.12.16.
 */

public class GameFragmentActivity extends FragmentStatePagerAdapter {

    private Configuration conf;
    private int numberOfFragments = 3;
    private String fragmentTitle[] = {"AchievementViewFragment", "CameraViewFragment", "MapViewFragment"};

    public GameFragmentActivity(FragmentManager fragmentManager, Configuration conf) {
        super(fragmentManager);
        this.conf = conf;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AchievementViewFragment();
            case 1:
                return new CameraViewFragment();
            case 2:
                return new MapViewFragment();
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
