package com.mikerinehart.geekrepublic.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.models.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArticleAdapter extends UltimateViewAdapter {

    private final String TAG = "NewsItemAdapter";
    private List<Post> postList;
    private Context c;

    public ArticleAdapter() {
        postList = new ArrayList<>();
    }

    public ArticleAdapter(List<Post> postList) {
        this.postList = postList;
    }

//    public void add(int position, Post p) {
//        postList.add(position, p);
//        notifyItemInserted(position);
//        insert(postList, p, );
//        //notifyItemRangeChanged(position-1, position);
//    }

    public void insert(Post p) {
        //insert(postList, p, getAdapterItemCount());
        postList.add(p);
    }

    public void clear() {
        postList.clear();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Post getPost(int position) {
        return postList.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int i) {
            // Try catch required to handle instance where "load more" view is injected and cannot be cast
            try {
                Post p = postList.get(i);
                Glide.with(c).load(p.getFeaturedImage().getSourceURL())
                        .into(((ViewHolder)vh).featuredImage);
                ((ViewHolder)vh).postTitle.setText(Html.fromHtml(p.getTitle()));
            } catch (ClassCastException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {

            }
    }

    @Override
    public int getAdapterItemCount() {
        return postList.size();
    }


    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        this.c = viewGroup.getContext();
        View itemView = LayoutInflater.from(c).inflate(R.layout.post_card_layout, viewGroup, false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
        @InjectView(R.id.post_card) protected CardView postCard;
        @InjectView(R.id.post_card_layout_post_title) protected TextView postTitle;
        @InjectView(R.id.post_card_layout_featured_image) protected ImageView featuredImage;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }

    }

}
