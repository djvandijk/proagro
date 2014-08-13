package isf.proagro.reader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Scrappy on 29-11-13.
 */
public class BookletBrowserFragment extends Fragment {

    private View _mainFragmentView;

    private LayoutInflater _inflater;

    private ViewGroup _container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        _inflater = inflater;
        _container = container;
        _mainFragmentView = inflater.inflate(R.layout.fragment_booklet_browser, container, false);

        UpdateBookletBrowser();

        return _mainFragmentView;
    }

    public void UpdateBookletBrowser()
    {
        LinearLayout layout = (LinearLayout) _mainFragmentView.findViewById(R.id.bookletsLayout);
        layout.removeAllViews();
        List<Booklet> booklets = BookletManager.GetAvailableBooklets();
        for(Booklet booklet : booklets)
        {
            ImageView newImageView = new ImageView(getActivity());
            newImageView.setImageBitmap(booklet.GetCover());
            final Booklet b = booklet;

            View bookletInfoFragment = _inflater.inflate(R.layout.view_booklet_info, layout, false);
            TextView titleLabel = (TextView) bookletInfoFragment.findViewById(R.id.titleLabel);
            TextView descriptionLabel = (TextView) bookletInfoFragment.findViewById(R.id.descriptionLabel);
            ImageButton bookletButton = (ImageButton) bookletInfoFragment.findViewById(R.id.bookletButton);

            titleLabel.setText(b.Title);
            descriptionLabel.setText(b.Description);
            bookletButton.setImageBitmap(booklet.GetCover());
            bookletButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ((MainActivity) getActivity()).OpenBooklet(b);
                }
            });
            layout.addView(bookletInfoFragment);
        }
    }
}
