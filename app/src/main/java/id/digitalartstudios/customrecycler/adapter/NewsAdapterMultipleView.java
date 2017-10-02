package id.digitalartstudios.customrecycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import id.digitalartstudios.customrecycler.R;
import id.digitalartstudios.customrecycler.config.AppConfig;
import id.digitalartstudios.customrecycler.model.NewsItem;

/**
 * Created by Ryan on 02/10/2017.
 */

public class NewsAdapterMultipleView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_GRID = 2;
    public static final int TYPE_LINEAR = 3;

    private Context context;
    private List<NewsItem> newsItemList;

    public NewsAdapterMultipleView(Context context, List<NewsItem> newsItems) {
        this.context = context;
        this.newsItemList = newsItems;
    }

    @Override
    public int getItemViewType (int position) {
        return newsItemList.get(position).getLayoutType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            return new TitleView(view);
        } else if (viewType == TYPE_HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderNewsView(view);
        } else if (viewType == TYPE_GRID) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
            return new GridNewsView(view);
        } else if (viewType == TYPE_LINEAR) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear, parent, false);
            return new LinearNewsView(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsItem newsItem = newsItemList.get(position);
        String URL_THUMB = AppConfig.IMAGES_300 + newsItem.getThumb();

        if (holder instanceof TitleView) {
            TitleView titleView = (TitleView) holder;
            titleView.tvTitle.setText(newsItem.getTitle());
        } else if (holder instanceof HeaderNewsView){
            final HeaderNewsView headerNewsView = (HeaderNewsView) holder;
            Glide.with(context).load(URL_THUMB)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    headerNewsView.imgBroken.setVisibility(View.GONE);
                    return false;
                }
            }).crossFade().into(headerNewsView.imgHeadThumb);
            headerNewsView.tvHeadTitle.setText(newsItem.getTitle());
            headerNewsView.tvHeadTime.setText(newsItem.getTime());
        } else if (holder instanceof LinearNewsView){
            LinearNewsView holdNewsView = (LinearNewsView) holder;
            Glide.with(context).load(URL_THUMB).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(holdNewsView.imgThumb);
            holdNewsView.tvTitle.setText(newsItem.getTitle());
            holdNewsView.tvTime.setText(newsItem.getTime());
        } else if (holder instanceof GridNewsView) {
            GridNewsView holdEditorView = (GridNewsView) holder;
            Glide.with(context).load(URL_THUMB)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(holdEditorView.ivThumb);
            holdEditorView.tvTitle.setText(newsItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return newsItemList == null ? 0 : newsItemList.size();
    }

    static class TitleView extends RecyclerView.ViewHolder{
        private final TextView tvTitle;

        TitleView(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.category_title);
        }
    }

    static class HeaderNewsView extends RecyclerView.ViewHolder{
        private final ImageView imgHeadThumb;
        private final ImageView imgBroken;
        private final TextView tvHeadTitle;
        private final TextView tvHeadTime;
        public HeaderNewsView(View itemView) {
            super(itemView);
            imgHeadThumb = itemView.findViewById(R.id.thumb_head);
            imgBroken = itemView.findViewById(R.id.image_broken);
            tvHeadTitle = itemView.findViewById(R.id.title_head);
            tvHeadTime = itemView.findViewById(R.id.time_head);
        }
    }

    static class GridNewsView extends RecyclerView.ViewHolder{
        private final TextView tvTitle;
        private final ImageView ivThumb;

        GridNewsView(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title);
            ivThumb = itemView.findViewById(R.id.thumb);
        }
    }

    static class LinearNewsView extends RecyclerView.ViewHolder{
        private final ImageView imgThumb;
        private final TextView tvTitle;
        private final TextView tvTime;
        public LinearNewsView(View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.thumb_info);
            tvTitle = itemView.findViewById(R.id.title_info);
            tvTime = itemView.findViewById(R.id.time_info);
        }
    }

}
