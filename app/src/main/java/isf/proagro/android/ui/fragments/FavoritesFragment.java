package isf.proagro.android.ui.fragments;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewFlipper;

import butterknife.InjectView;
import io.realm.RealmResults;
import isf.proagro.android.R;
import isf.proagro.android.model.Booklet;
import isf.proagro.android.ui.abstracts.BaseFragment;
import isf.proagro.android.ui.activities.MainActivity;
import isf.proagro.android.ui.adapters.BookletAdapter;
import isf.proagro.android.ui.widgets.BookletRecyclerView;
import isf.proagro.android.utils.AndroidUtils;

public class FavoritesFragment extends BaseFragment {

    public static final int PROGRESS    = 0;
    public static final int CONTENT     = 1;
    public static final int NO_CONTENT       = 2;

    @InjectView(R.id.recycleview)
    protected BookletRecyclerView mRecycleview;

    @InjectView(R.id.view_flipper)
    protected ViewFlipper mViewFlipper;

    private BookletAdapter mAdapter;

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FavoritesFragment() {
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
        mRecycleview.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.layout_no_booklet, null, false));
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.numbers_of_colums), LinearLayoutManager.VERTICAL, false));
        mAdapter = new BookletAdapter(getActivity(), null, false);
        mRecycleview.setAdapter(mAdapter);
        updateBooklets();
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
        return R.layout.fragment_favorites;
    }


    private RealmResults<Booklet> getBookletFromDatabase() {
        RealmResults<Booklet> results = getRealm().where(Booklet.class)
                .equalTo("language", AndroidUtils.getLangage())
                .equalTo("isFavorite", true)
                .findAll();
        results.sort("title");
        return results;
    }

    private void updateBooklets() {
        mViewFlipper.setDisplayedChild(PROGRESS);
        RealmResults<Booklet> results = getBookletFromDatabase();
        if (results.size() == 0) {
            mViewFlipper.setDisplayedChild(NO_CONTENT);
        } else {
            mAdapter.addData(results);
            mViewFlipper.setDisplayedChild(CONTENT);
        }
    }

}
