package com.mikerinehart.geekrepublic.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.models.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final String TAG = "NewsItemAdapter";
    private List<Post> postList;
    private Context c;

    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int lastAnimatedPosition = -1;
    private int itemsCount = 0;


    public PostAdapter() {
        postList = new ArrayList<Post>();
    }

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    public void add(int position, Post p) {
        postList.add(position, p);
        notifyItemInserted(position);
        notifyItemRangeChanged(position-1, position);
    }

    public void clear() {
        postList.clear();
    }

//    public void updateList(List<Post> newPostList) {
//        postList.clear();
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int i) {
        Post p = postList.get(i);
        Glide.with(c).load(p.getFeaturedImage().getSourceURL())
                .into(vh.featuredImage);
        vh.postTitle.setText(Html.fromHtml(p.getTitle()));
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.c = viewGroup.getContext();
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_card_layout, viewGroup, false);
        return new ViewHolder(itemView);
    }

    //Holds the Ride cardviews
    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.post_card) CardView postCard;
        @InjectView(R.id.post_card_layout_post_title) TextView postTitle;
        @InjectView(R.id.post_card_layout_featured_image) ImageView featuredImage;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }

    }

}
