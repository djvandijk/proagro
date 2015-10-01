package isf.proagro.android.ui.abstracts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import isf.proagro.android.R;

/**
 * Created by eddyhugues on 15-05-17.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Optional
    @InjectView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.inject(this);
    }

    public abstract int getLayoutId();

}
