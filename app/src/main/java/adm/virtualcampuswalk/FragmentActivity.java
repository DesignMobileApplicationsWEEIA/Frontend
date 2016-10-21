package adm.virtualcampuswalk;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import adm.virtualcampuswalk.fragments.CameraViewFragment;

/**
 * Created by Adam Piech on 2016-10-20.
 */

public class FragmentActivity extends FragmentStatePagerAdapter {

    private Configuration conf;
    private int numberOfFragments = 1;
    private String fragmentTitle[] = {"CameraViewFragment"};

    public FragmentActivity(FragmentManager fragmentManager, Configuration conf) {
        super(fragmentManager);
        this.conf = conf;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CameraViewFragment();
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
