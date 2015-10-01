package isf.proagro.android.ui.abstracts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.realm.Realm;
import isf.proagro.android.ProAgroApplication;
import isf.proagro.android.rest.RestManager;

/**
 * Created by eddyhugues on 15-05-17.
 */
public abstract class BaseFragment extends Fragment {

    protected RestManager mRestService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestService = new RestManager(getActivity());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayoutId(), container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    protected abstract int getFragmentLayoutId();

    public RestManager getRestService() {
        return mRestService;
    }

    public Realm getRealm() {
        return ((ProAgroApplication)getActivity().getApplicationContext()).getRealm();
    }
}
