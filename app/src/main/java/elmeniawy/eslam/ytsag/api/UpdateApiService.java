package elmeniawy.eslam.ytsag.api;

import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * UpdateApiService
 * <p>
 * Created by Eslam El-Meniawy on 13-Mar-2018.
 * CITC - Mansoura University
 */

public interface UpdateApiService {
    @GET("AppData.json#{current_time}")
    Observable<UpdateResponse> checkUpdate(@Path("current_time") long currentTime);
}
