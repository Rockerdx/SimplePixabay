package rizky.rockerdx.picbayloader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ImageAdapter";
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int DIVIDER = 2;


    private List<Hit> hitList;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String currentDate = "";

    private PaginationAdapterCallback mCallback;

    ImageAdapter(List<Hit> hitList, PaginationAdapterCallback callback) {
        setHitList(hitList);
        this.mCallback = callback;
    }

    private void setHitList(List<Hit> hitList) {
        if (hitList == null) {
            this.hitList = new ArrayList<>();
        } else {
            this.hitList = hitList;
        }
    }


    List<Hit> getHitList() {
        return hitList;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == hitList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.image_layout, parent, false);
                viewHolder = new DataViewHolder(viewItem);
                break;
            case LOADING:
            default:
                View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new EmptyHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Hit article = hitList.get(position); // Movie

        switch (getItemViewType(position)) {

            case ITEM:
                DataViewHolder newsViewHolder = (DataViewHolder) holder;
                newsViewHolder.bind(article);
                break;
            case LOADING:
                EmptyHolder loadingVH = (EmptyHolder) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText("Failed to fetch data");

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (hitList != null && hitList.size() > 0) {
            return hitList.size();
        } else {
            return 1;
        }
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        DataViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void bind(Hit hit) {
            Glide.with(itemView)
                    .load(hit.getWebformatURL())
                    .into(imageView);
        }
    }

    class EmptyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ProgressBar mProgressBar;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        EmptyHolder(@NonNull View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            ImageButton mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.loadmore_retry || id == R.id.loadmore_errorlayout) {
                showRetry(false);
                mCallback.retryPageLoad();
            }
        }
    }

    void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Hit());
    }

    void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = hitList.size() - 1;
        Hit article = getItem(position);

        if (article != null) {
            hitList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Hit getItem(int position) {
        return hitList.get(position);
    }

    void showRetry(boolean show) {
        retryPageLoad = show;
        notifyItemChanged(hitList.size() - 1);
    }

    private void add(Hit article) {
        hitList.add(article);
        notifyItemInserted(hitList.size() - 1);
    }

    void addAll(List<Hit> articleList) {
        this.hitList.addAll(articleList);
    }


    public interface PaginationAdapterCallback {

        void retryPageLoad();
    }

}
