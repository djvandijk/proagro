package isf.proagro.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by eddyhugues on 15-05-19.
 */
public class ProAgroApplication extends Application {

    protected Realm mRealm;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mRealm = Realm.getInstance(this);
    }

    public synchronized Realm getRealm() {
        return mRealm;
    }

}
