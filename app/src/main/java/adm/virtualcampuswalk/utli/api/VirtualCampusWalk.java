package adm.virtualcampuswalk.utli.api;

import adm.virtualcampuswalk.models.Building;
import adm.virtualcampuswalk.models.PhoneData;
import adm.virtualcampuswalk.models.Result;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mariusz on 12.11.16.
 */

public interface VirtualCampusWalk {

    @POST("Building/search")
    Call<Result<Building>> getBuilding(@Body PhoneData phoneData);

}
