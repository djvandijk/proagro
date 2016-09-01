package isf.proagro.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by eddyhugues on 15-05-19.
 */
public class ProAgroApplication extends Application {

    protected Realm mRealm;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // TODO 20160901/tito: really not ideal but the project has not been using migrations until
        // now so I don't know which versions of the DB most people have installed. We can take
        // a look at this again once we upgrade to the newest version of Realm.
        try {
            mRealm = Realm.getInstance(this);
        } catch (RealmMigrationNeededException _) {
            Realm.deleteRealmFile(this);
            mRealm = Realm.getInstance(this);
        }
    }

    public synchronized Realm getRealm() {
        return mRealm;
    }

}
