package isf.proagro.android.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;
import isf.proagro.android.R;
import isf.proagro.android.listeners.ItemClickListener;
import isf.proagro.android.model.Category;
import isf.proagro.android.ui.activities.MainActivity;
import isf.proagro.android.ui.fragments.BookletsFragment;

public class KnowledgeSharingAdapter extends RecyclerView.Adapter<KnowledgeSharingAdapter.ViewHolder> implements ItemClickListener {

    private RealmResults<Category> mCategories;
    private Context mContext;

    public KnowledgeSharingAdapter(Context context, RealmResults<Category> categories) {
        mCategories = categories;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.knowledgesharing_row, parent, false), this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category item = mCategories.get(position);
        holder.title.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mCategories == null ? 0 : mCategories.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        Category c = mCategories.get(position);
        ((MainActivity) mContext).addBookletFragmentToContainer(BookletsFragment.newInstance(c), c.getName());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.title)
        public TextView title;
        @InjectView(R.id.ripple)
        MaterialRippleLayout rippleLayout;

        ItemClickListener mItemClickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            mItemClickListener = itemClickListener;
            rippleLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition());
        }

    }

    public void addData(RealmResults<Category> results) {
        mCategories = results;
        notifyDataSetChanged();
    }
}
