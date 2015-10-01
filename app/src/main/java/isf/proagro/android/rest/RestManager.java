package isf.proagro.android.rest;


import android.content.Context;

import isf.proagro.android.R;
import isf.proagro.android.model.Booklet;
import isf.proagro.android.utils.AndroidUtils;
import isf.proagro.android.utils.NetworkUtils;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.SimpleXMLConverter;
import rx.Observable;

/**
 * Created by eddyhugues on 15-05-17.
 */
public class RestManager {

    private final RestAPI mWebService;

    public RestManager(Context context) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.base_url))
                .setConverter(new SimpleXMLConverter())
//                .setRequestInterceptor(NetworkUtils.getInterceptor(context))
//                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setClient(new OkClient(NetworkUtils.getHttpClient(context)))
                .build();

        mWebService = restAdapter.create(RestAPI.class);
    }

    public RestAPI getWebService() {
        return mWebService;
    }

    public Observable<Booklet> fetchAllBooklet() {
        return mWebService.getLibrary(AndroidUtils.getLangage())
                .flatMap(library -> Observable.from(library.getBooklets()))
                .flatMap(path -> mWebService.getBooklet(AndroidUtils.getLangage(), path));
    }
}
