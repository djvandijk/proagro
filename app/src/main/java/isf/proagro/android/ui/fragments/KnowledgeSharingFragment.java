package isf.proagro.android.ui.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewFlipper;

import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.RealmResults;
import isf.proagro.android.R;
import isf.proagro.android.model.Category;
import isf.proagro.android.ui.abstracts.BaseFragment;
import isf.proagro.android.ui.activities.MainActivity;
import isf.proagro.android.ui.adapters.KnowledgeSharingAdapter;
import isf.proagro.android.ui.widgets.SimpleRecyclerView;
import isf.proagro.android.utils.AndroidUtils;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class KnowledgeSharingFragment extends BaseFragment {

    public static final int PROGRESS    = 0;
    public static final int CONTENT     = 1;
    public static final int ERROR       = 2;

    @InjectView(R.id.recycleview)
    protected SimpleRecyclerView mRecycleview;

    @InjectView(R.id.view_flipper)
    protected ViewFlipper mViewFlipper;

    private KnowledgeSharingAdapter mAdapter;
    private Subscription subscription;

    public static KnowledgeSharingFragment newInstance() {
        KnowledgeSharingFragment fragment = new KnowledgeSharingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public KnowledgeSharingFragment() {
    }

    @Override
    public void onDestroy() {
        unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new KnowledgeSharingAdapter(getActivity(), null);
        mRecycleview.setAdapter(mAdapter);

        RealmResults<Category> results = getCategoriesFromDatabase();
        if (results.size() == 0) {
            updateCategories();
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
            updateCategories();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_knowledgesharing;
    }

    @OnClick(R.id.retry)
    protected void updateCategories() {
        unsubscribe();

        subscription = AppObservable.bindFragment(this, mRestService.fetchAllCategories(getLanguage()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        xmlCategory -> createCategory(xmlCategory),
                        error -> displayDataWhenUnavailable(),
                        () -> fillAdapterWithContent());
    }

    private String getLanguage() {
        return AndroidUtils.getLangage();
    }

    private void createCategory(Category xmlCategory) {
        getRealm().beginTransaction();
        AndroidUtils.createRealmCategory(getRealm(), getLanguage(), xmlCategory);
        getRealm().commitTransaction();
    }

    private RealmResults<Category> getCategoriesFromDatabase() {
        RealmResults<Category> results = getRealm().where(Category.class).equalTo("language", getLanguage()).findAll();
        results.sort("name");
        return results;
    }

    private void displayDataWhenUnavailable() {
        if (getCategoriesFromDatabase().size() > 0) {
            fillAdapterWithContent();
        } else {
            mViewFlipper.setDisplayedChild(ERROR);
        }
    }

    private void fillAdapterWithContent() {
        mAdapter.addData(getCategoriesFromDatabase());
        mRecycleview.scrollToPosition(0);
        if (mViewFlipper.getDisplayedChild() != CONTENT)
            mViewFlipper.setDisplayedChild(CONTENT);
    }

    private void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }
}
