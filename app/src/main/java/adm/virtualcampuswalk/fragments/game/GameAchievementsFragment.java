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
import adm.virtualcampuswalk.models.PhoneData;
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

    private VirtualCampusWalk virtualCampusWalk;
    private MacReader mac = new SimpleMacReader();

    private TextView titleTextView;
    private ImageView medalImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.achievements_activity, container, false);
        initVirtualCampusWalk();

        titleTextView = (TextView) inflate.findViewById(R.id.achievementTitle);
        medalImageView = (ImageView) inflate.findViewById(R.id.achievementImage);
        setAchievement("Zbierz wszystkie znaczniki", R.mipmap.gold_medal);

        return inflate;
    }

    private void call(PhoneData phoneData) {
        Call<Result<List<Achievement>>> achievements = virtualCampusWalk.getAchievements(new MacDto(mac.getMacAddress()));
        achievements.enqueue(
                new Callback<Result<List<Achievement>>>() {
                    @Override
                    public void onResponse(Call<Result<List<Achievement>>> call, Response<Result<List<Achievement>>> response) {
                        Log.i(TAG, "onResponse: " + response.isSuccessful() + " " + response.body());
                        if (response.isSuccessful() && response.body().isSuccess()) {
                            for (Achievement achievement : response.body().getValue()) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<List<Achievement>>> call, Throwable throwable) {
                        Log.e(TAG, "getAchievements: ", throwable);
                    }
                }
        );
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

    @Override
    public void onResume() {
        super.onResume();
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
