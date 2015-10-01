package isf.proagro.android.ui.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import isf.proagro.android.R;
import isf.proagro.android.ui.abstracts.BaseFragment;

public class AboutFragment extends BaseFragment {

    public AboutFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Link link = new Link(getString(R.string.isf_website))
                .setOnClickListener(s -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://"+getString(R.string.isf_website)));
                    startActivity(i);
                });

        LinkBuilder.on((TextView) view.findViewById(R.id.about))
                .addLink(link)
                .build();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_about;
    }
}
