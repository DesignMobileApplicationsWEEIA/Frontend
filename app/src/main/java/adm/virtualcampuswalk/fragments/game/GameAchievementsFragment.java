package adm.virtualcampuswalk.fragments.game;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.fragments.PositionServiceFragment;
import adm.virtualcampuswalk.models.Achievement;
import adm.virtualcampuswalk.models.MacDto;
import adm.virtualcampuswalk.models.Result;
import adm.virtualcampuswalk.utli.api.VirtualCampusWalk;
import adm.virtualcampuswalk.utli.network.MacReader;
import adm.virtualcampuswalk.utli.network.SimpleMacReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static adm.virtualcampuswalk.utli.Util.BASE_URL;
import static adm.virtualcampuswalk.utli.Util.TAG;

/**
 * Created by Adam Piech on 2016-12-07.
 */

public class GameAchievementsFragment extends PositionServiceFragment {

    private static final String ACHIEVEMENT_TITLE = "Zbierz wszystkie znaczniki";
    private VirtualCampusWalk virtualCampusWalk;
    private MacReader macReader = new SimpleMacReader();

    private TextView titleTextView;
    private ImageView medalImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.achievements_activity, container, false);
        initVirtualCampusWalk();
        initView(inflate);
        call();
        return inflate;
    }

    private void call() {
        MacDto mac = new MacDto(macReader.getMacAddress());
        Log.i(TAG, "call: " + mac.getMac());
        Call<Result<List<Achievement>>> achievements = virtualCampusWalk.getAchievements(mac);
        achievements.enqueue(
                new Callback<Result<List<Achievement>>>() {
                    @Override
                    public void onResponse(Call<Result<List<Achievement>>> call, Response<Result<List<Achievement>>> response) {
                        Log.i(TAG, "onResponse: " + response.isSuccessful() + " " + response.body());
                        if (response.isSuccessful() && response.body().isSuccess()) {
                            assignAchievementLevel(response.body().getValue());
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<List<Achievement>>> call, Throwable throwable) {
                        Log.e(TAG, "getAchievements: ", throwable);
                    }
                }
        );
    }

    private void assignAchievementLevel(List<Achievement> achievements) {
        double achievementLevel = countPercentageOfCompletedTasks(achievements);
        if (achievementLevel < 0.5) {
            setAchievement(ACHIEVEMENT_TITLE, R.mipmap.game);
        }
        if (achievementLevel >= 0.5 && achievementLevel < 0.75) {
            setAchievement(ACHIEVEMENT_TITLE, R.mipmap.bronze_medal);
        }
        if (achievementLevel >= 0.75 && achievementLevel < 1.0) {
            setAchievement(ACHIEVEMENT_TITLE, R.mipmap.silver_medal);
        }
        if (Math.abs(achievementLevel - 1.0) < 0.0001) {
            setAchievement(ACHIEVEMENT_TITLE, R.mipmap.gold_medal);
        }
    }

    private double countPercentageOfCompletedTasks(List<Achievement> achievements) {
        int quantity = achievements.size();
        int completedAchievements = 0;
        for (Achievement achievement : achievements) {
            if (achievement.isCompleted()) {
                completedAchievements++;
            }
        }
        if (completedAchievements != 0) {
            return (double) completedAchievements / (double) quantity;
        }
        return 0.0;
    }

    private void initVirtualCampusWalk() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        virtualCampusWalk = retrofit.create(VirtualCampusWalk.class);
    }

    private void setAchievement(String title, Integer medal) {
        titleTextView.setText(title);
        if (medal != null) {
            medalImageView.setImageResource(medal);
        }
    }

    private void initView(View inflate) {
        titleTextView = (TextView) inflate.findViewById(R.id.achievementTitle);
        medalImageView = (ImageView) inflate.findViewById(R.id.achievementImage);
    }

    @Override
    public void onResume() {
        super.onResume();
        call();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
