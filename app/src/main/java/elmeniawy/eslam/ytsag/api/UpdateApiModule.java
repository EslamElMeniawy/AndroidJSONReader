package elmeniawy.eslam.ytsag.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import elmeniawy.eslam.ytsag.utils.NetworkUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * UpdateApiModule
 * <p>
 * Created by Eslam El-Meniawy on 13-Mar-2018.
 * CITC - Mansoura University
 */

@Module
public class UpdateApiModule {
    @Provides
    @Singleton
    OkHttpClient provideClient() {
        return new OkHttpClient
                .Builder()
                .addInterceptor(provideLoggingInterceptor())
                .build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(String baseURL, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addCallAdapterFactory(provideRxFactory())
                .addConverterFactory(provideGsonFactory())
                .build();
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory provideRxFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    UpdateApiService provideApiService() {
        return provideRetrofit(NetworkUtils.UPDATE_API_BASE_URL, provideClient())
                .create(UpdateApiService.class);
    }
}
