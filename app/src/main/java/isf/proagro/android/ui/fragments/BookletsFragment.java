package isf.proagro.android.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewFlipper;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import isf.proagro.android.R;
import isf.proagro.android.model.Booklet;
import isf.proagro.android.ui.abstracts.BaseFragment;
import isf.proagro.android.ui.activities.MainActivity;
import isf.proagro.android.ui.adapters.BookletAdapter;
import isf.proagro.android.ui.widgets.BookletRecyclerView;
import isf.proagro.android.utils.AndroidUtils;
import retrofit.mime.TypedByteArray;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookletsFragment extends BaseFragment {

    public static final int PROGRESS    = 0;
    public static final int CONTENT     = 1;
    public static final int ERROR       = 2;

    @InjectView(R.id.recycleview)
    protected BookletRecyclerView mRecycleview;

    @InjectView(R.id.view_flipper)
    protected ViewFlipper mViewFlipper;

    private BookletAdapter mAdapter;

    public static BookletsFragment newInstance() {
        BookletsFragment fragment = new BookletsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BookletsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mRecycleview.setEmptyView(view.findViewById(android.R.id.empty));
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.numbers_of_colums), LinearLayoutManager.VERTICAL, false));
        mAdapter = new BookletAdapter(getActivity(), null);
        mRecycleview.setAdapter(mAdapter);
        RealmResults<Booklet> results = getBookletFromDatabase();
        if (results.size() == 0) {
            updateBooklets();
        } else {
            mAdapter.addData(results);
            mViewFlipper.setDisplayedChild(CONTENT);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!((MainActivity) getActivity()).isNavigationDraweOpen())
            inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mViewFlipper.setDisplayedChild(PROGRESS);
            updateBooklets();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_booklets;
    }

    @OnClick(R.id.retry)
    protected void updateBooklets() {
        AppObservable.bindFragment(this, mRestService.fetchAllBooklet())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(booklet -> downLoadPdfAndSaveBooklet(booklet),
                        error -> displayDataWhenUnaivalable(),
                        () -> fillAdapterWithContent());
    }

    private void downLoadPdfAndSaveBooklet(Booklet booklet) {
        AppObservable.bindFragment(this, mRestService.getWebService().getBookletPDF(AndroidUtils.getLangage(), booklet.getPdf()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            File pdf = new File(getActivity().getFilesDir() + "/" + booklet.getPdf());
                            boolean isFavorite = false;
                            if (pdf.exists()) {
                                pdf.delete();
                                RealmQuery<Booklet> query = getRealm().where(Booklet.class);
                                query.equalTo("title", booklet.getTitle());
                                query.equalTo("language", booklet.getLanguage());
                                query.equalTo("description", booklet.getDescription());
                                query.equalTo("pdf", booklet.getPdf());
                                query.equalTo("cover", booklet.getCover());
                                RealmResults<Booklet> realmResults = query.findAll();
                                getRealm().beginTransaction();
                                isFavorite = realmResults.get(0).isFavorite();
                                realmResults.clear();
                                getRealm().commitTransaction();
                            }

                            FileOutputStream outputStream;
                            try {
                                outputStream = getActivity().openFileOutput(booklet.getPdf(), Context.MODE_PRIVATE);
                                outputStream.write(((TypedByteArray) response.getBody()).getBytes());
                                outputStream.close();
                                booklet.setPdfPath(getActivity().getFilesDir() + "/" + booklet.getPdf());
                                getRealm().beginTransaction();
                                AndroidUtils.getRealmBooklet(getRealm(), booklet, isFavorite);
                                getRealm().commitTransaction();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        },
                        error -> displayDataWhenUnaivalable(),
                        () -> fillAdapterWithContent());
    }

    private RealmResults<Booklet> getBookletFromDatabase() {
        RealmResults<Booklet> results = getRealm().where(Booklet.class).equalTo("language",AndroidUtils.getLangage()).findAll();
        results.sort("title");
        return results;
    }

    private void displayDataWhenUnaivalable() {
        if (getBookletFromDatabase().size() > 0) {
            fillAdapterWithContent();
        } else {
            mViewFlipper.setDisplayedChild(ERROR);
        }
    }

    private void fillAdapterWithContent() {
        mAdapter.addData(getBookletFromDatabase());
        mRecycleview.scrollToPosition(0);
        if (mViewFlipper.getDisplayedChild() != CONTENT)
            mViewFlipper.setDisplayedChild(CONTENT);
    }

}
