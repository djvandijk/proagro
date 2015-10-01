package isf.proagro.android.utils;

import android.content.Context;
import android.content.Intent;


import isf.proagro.android.model.Booklet;
import isf.proagro.android.ui.activities.BookletDetailActivity;

/**
 * Created by eddyhugues on 15-05-20.
 */
public class IntentUtils {

    public static final String BOOKLET_TITLE    = "booklet_title";
    public static final String BOOKLET_PDF_PATH = "booklet_pdf_path";

    public static Intent getBookletDetailsActivityIntent(Context context, Booklet booklet) {
        Intent intent = new Intent(context, BookletDetailActivity.class);
        intent.putExtra(BOOKLET_TITLE, booklet.getTitle());
        intent.putExtra(BOOKLET_PDF_PATH, booklet.getPdfPath());
        return intent;
    }

}
