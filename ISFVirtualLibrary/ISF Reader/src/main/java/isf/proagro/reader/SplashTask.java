package isf.proagro.reader;

import android.os.AsyncTask;

import com.radaee.pdf.Global;

/**
 * Created by Dirk-Jan on 22-12-13.
 */
public class SplashTask extends AsyncTask<Void, Void, Void> {

    private SplashActivity _activity;

    public SplashTask(SplashActivity activity)
    {
        _activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BookletManager.Init(_activity);
        Global.Init(_activity);
        _activity.SetInitialized(true);
        return null;
    }
}
