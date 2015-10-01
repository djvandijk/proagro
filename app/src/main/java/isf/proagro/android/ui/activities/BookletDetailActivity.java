package isf.proagro.android.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ViewSwitcher;

import com.joanzapata.pdfview.PDFView;

import java.io.File;

import butterknife.InjectView;
import isf.proagro.android.R;
import isf.proagro.android.ui.abstracts.BaseActivity;
import isf.proagro.android.utils.AndroidUtils;
import isf.proagro.android.utils.IntentUtils;

public class BookletDetailActivity extends BaseActivity {

    public static final int PROGRESS    = 0;
    public static final int CONTENT     = 1;

    public static final String CURRENT_PAGE = "current_page";


    @InjectView(R.id.view_switcher)
    protected ViewSwitcher mViewSwitcher;

    @InjectView(R.id.pdfview)
    protected PDFView mPdfView;

    private String mBookletTitle, mBookletPdfPath;
    private int mCurrentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mBookletTitle = getIntent().getStringExtra(IntentUtils.BOOKLET_TITLE);
            mBookletPdfPath = getIntent().getStringExtra(IntentUtils.BOOKLET_PDF_PATH);
        } else {
            mBookletTitle = savedInstanceState.getString(IntentUtils.BOOKLET_TITLE);
            mBookletPdfPath = savedInstanceState.getString(IntentUtils.BOOKLET_PDF_PATH);
            mCurrentPage = savedInstanceState.getInt(CURRENT_PAGE, 0);
        }
        AndroidUtils.initActionBar(this, mToolbar);
        setTitle(mBookletTitle);

        File file = new File(mBookletPdfPath);

        mPdfView.fromFile(file)
                .enableSwipe(true)
                .defaultPage(mCurrentPage)
                .onLoad(i -> mViewSwitcher.setDisplayedChild(CONTENT))
                .load();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_booklet_detail;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IntentUtils.BOOKLET_TITLE, mBookletTitle);
        outState.putString(IntentUtils.BOOKLET_PDF_PATH, mBookletPdfPath);
        outState.putInt(CURRENT_PAGE, mPdfView.getCurrentPage());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
