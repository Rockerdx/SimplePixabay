package rizky.rockerdx.picbayloader;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("api")
    Observable<Data> getEverything(
            @Query("q") String query,
            @Query("key") String key,
            @Query("page") int page

    );
}
