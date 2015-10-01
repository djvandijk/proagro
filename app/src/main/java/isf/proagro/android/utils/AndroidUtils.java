package isf.proagro.android.utils;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;

import java.util.Locale;

import io.realm.Realm;
import isf.proagro.android.R;
import isf.proagro.android.model.Booklet;

/**
 * Created by eddyhugues on 15-05-17.
 */
public class AndroidUtils {

    private static final String EN = "en";
    private static final String FR = "fr";

    public static final String getLangage() {
        return TextUtils.equals(EN, Locale.getDefault().getLanguage()) ? EN : FR;
    }

    public static Booklet getRealmBooklet(Realm realm, Booklet booklet, boolean isFavorite) {
        Booklet b = realm.createObject(Booklet.class);
        b.setTitle(booklet.getTitle());
        b.setDescription(booklet.getDescription());
        b.setPdf(booklet.getPdf());
        b.setCover(booklet.getCover());
        b.setLanguage(booklet.getLanguage());
        b.setIsFavorite(isFavorite);
        b.setPdfPath(booklet.getPdfPath());
        return b;
    }

    public static int getScreenHeight(Activity act) {
        int height = 0;
        Display display = act.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight();  // deprecated
        }
        return height;
    }

    public static void initActionBar(AppCompatActivity activity, Toolbar toolbar) {
        if (toolbar != null && activity != null) {
            toolbar.setNavigationIcon(R.drawable.ic_back);
            activity.setSupportActionBar(toolbar);
            if (activity.getActionBar() != null) {
                activity.getActionBar().setDisplayShowHomeEnabled(true);
                activity.getActionBar().setHomeButtonEnabled(true);
            }
        }
    }


}
