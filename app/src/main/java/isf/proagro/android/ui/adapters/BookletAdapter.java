package isf.proagro.android.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;
import isf.proagro.android.ProAgroApplication;
import isf.proagro.android.R;
import isf.proagro.android.listeners.ItemClickListener;
import isf.proagro.android.model.Booklet;
import isf.proagro.android.ui.widgets.CheckedImageView;
import isf.proagro.android.ui.widgets.ScaleImageView;
import isf.proagro.android.utils.AndroidUtils;
import isf.proagro.android.utils.IntentUtils;

/**
 * Created by eddyhugues on 15-05-19.
 */
public class BookletAdapter extends RecyclerView.Adapter<BookletAdapter.ViewHolder> implements ItemClickListener {

    private RealmResults<Booklet> mBooklets;
    private Context mContext;
    private Realm mRealm;
    private boolean mDisplayBookmark = true;

    public BookletAdapter(Context context, RealmResults<Booklet> booklets) {
        this.mBooklets = booklets;
        mContext = context;
        mRealm = ((ProAgroApplication) mContext.getApplicationContext()).getRealm();
    }

    public BookletAdapter(Context context, RealmResults<Booklet> booklets, boolean displayBookmark) {
        this.mBooklets = booklets;
        mContext = context;
        mRealm = ((ProAgroApplication) mContext.getApplicationContext()).getRealm();
        mDisplayBookmark = displayBookmark;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.booklet_row, parent, false), this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Booklet item = mBooklets.get(position);

        if (!TextUtils.isEmpty(item.getCover())) {
            Glide.with(mContext)
                    .load(Uri.parse(mContext.getString(R.string.base_url) + "/library/" + AndroidUtils.getLangage() + "/" + item.getCover()))
                    .centerCrop()
                    .crossFade()
                    .into(holder.image);
        }
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        if (mDisplayBookmark) {
            holder.bookmark.setChecked(item.isFavorite());
        } else {
            holder.bookmark.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mBooklets == null ? 0 : mBooklets.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view instanceof CheckedImageView) {
            CheckedImageView bookmark = (CheckedImageView) view;
            bookmark.setChecked(!bookmark.isChecked());
            mRealm.beginTransaction();
            mBooklets.get(position).setIsFavorite(bookmark.isChecked());
            mRealm.commitTransaction();
            notifyDataSetChanged();
        } else {
            mContext.startActivity(IntentUtils.getBookletDetailsActivityIntent(mContext, mBooklets.get(position)));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.image)
        public ScaleImageView image;
        @InjectView(R.id.title)
        public TextView title;
        @InjectView(R.id.description)
        public TextView description;
        @InjectView(R.id.bookmark)
        CheckedImageView bookmark;
        @InjectView(R.id.ripple)
        MaterialRippleLayout rippleLayout;


        ItemClickListener mItemClickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            mItemClickListener = itemClickListener;
            rippleLayout.setOnClickListener(this);
            bookmark.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition());
        }

    }

    public void addData(RealmResults results) {
        mBooklets = results;
        notifyDataSetChanged();
    }

}
