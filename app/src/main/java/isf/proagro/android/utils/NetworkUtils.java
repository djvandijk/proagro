package isf.proagro.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import retrofit.RequestInterceptor;

/**
 * Created by eddyhugues on 28/03/15.
 */
public class NetworkUtils {

    public static OkHttpClient getHttpClient(final Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), context.getPackageName());
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(cache);
        return okHttpClient;
    }

    public static RequestInterceptor getInterceptor(final Context context) {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json;versions=1");
                if (NetworkUtils.isNetworkAvailable(context)) {
                    int maxAge = 60; // read from cache for 1 minute
                    request.addHeader("Cache-Control", "public, max-age=" + maxAge);
                } else {
                    int maxStale = 60 * 60 * 24 * 7 * 4; // tolerate 4-weeks stale
                    request.addHeader("Cache-Control",
                            "public, only-if-cached, max-stale=" + maxStale);
                }
            }
        };
    }

    public static boolean isNetworkAvailable(final Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
