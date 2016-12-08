package adm.virtualcampuswalk.fragments.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.fragments.PositionServiceFragment;

/**
 * Created by Adam Piech on 2016-12-07.
 */

public class GameAchievementsFragment extends PositionServiceFragment {

    private TextView titleTextView;
    private ImageView medalImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.achievements_activity, container, false);
        titleTextView = (TextView) view.findViewById(R.id.achievementTitle);
        medalImageView = (ImageView) view.findViewById(R.id.achievementImage);
        setAchievement("Zbierz wszystkie zanczniki", R.mipmap.gold_medal);
        return view;
    }

    private void setAchievement(String title, Integer medal) {
        titleTextView.setText(title);
        if (medal != null) {
            medalImageView.setImageResource(medal);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
