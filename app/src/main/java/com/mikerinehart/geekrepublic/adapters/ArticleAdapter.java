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


    private List<Post> articleList;
    private Context mContext;

    public ArticleAdapter() {
        this.articleList = new ArrayList<>();
    }

//    public ArticleAdapter(List<Post> articleList) {
//        this.articleList = articleList;
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position < getItemCount()
                && (customHeaderView != null ? position <= articleList.size() : position < articleList.size())
                && (customHeaderView != null ? position > 0 : true)) {
            Post p = articleList.get(position);
            ((ViewHolder)holder).postTitle.setText(Html.fromHtml(p.getTitle()));
            Glide.with(mContext).load(p.getFeaturedImage().getSourceURL())
                    .into(((ViewHolder)holder).featuredImage);
            ((ViewHolder)holder).postTitle.setText(Html.fromHtml(p.getTitle()));
        }
    }

    @Override
    public int getAdapterItemCount() {

        return articleList.size();
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        this.mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    public void insert(Post p, int position) {
        insert(articleList, p, position);
    }

    public void remove(int position) {
        remove(articleList, position);
    }

    public void clear() {
        clear(articleList);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    public void swapPositions(int from, int to) {
        swapPositions(articleList, from, to);
    }


    @Override
    public long generateHeaderId(int position) {
//        // URLogs.d("position--" + position + "   " + getItem(position));
//        if (getItem(position).length() > 0)
//            return getItem(position).charAt(0);
//        else return -1;
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.stick_header_item, viewGroup, false);
//        return new RecyclerView.ViewHolder(view) {
//        };
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

//        TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.stick_text);
//        textView.setText(String.valueOf(getItem(position).charAt(0)));
////        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AA70DB93"));
//        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AAffffff"));
//        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.stick_img);
//
//        SecureRandom imgGen = new SecureRandom();
//        switch (imgGen.nextInt(3)) {
//            case 0:
//                imageView.setImageResource(R.drawable.test_back1);
//                break;
//            case 1:
//                imageView.setImageResource(R.drawable.test_back2);
//                break;
//            case 2:
//                imageView.setImageResource(R.drawable.test_back);
//                break;
//        }

    }
//
//    private int getRandomColor() {
//        SecureRandom rgen = new SecureRandom();
//        return Color.HSVToColor(150, new float[]{
//                rgen.nextInt(359), 1, 1
//        });
//    }


    class ViewHolder extends UltimateViewAdapter.UltimateRecyclerviewViewHolder {

        @InjectView(R.id.post_card) protected CardView postCard;
        @InjectView(R.id.post_card_layout_post_title) protected TextView postTitle;
        @InjectView(R.id.post_card_layout_featured_image) protected ImageView featuredImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public Post getItem(int position) throws Exception {
        if (customHeaderView != null)
            position--;
        if (position < articleList.size())
            return articleList.get(position);
        else return null;
    }


//    private final String TAG = "NewsItemAdapter";
//    private List<Post> postList;
//    private Context c;
//
//    public ArticleAdapter() {
//        postList = new ArrayList<>();
//    }
//
//    public ArticleAdapter(List<Post> postList) {
//        this.postList = postList;
//    }
//
//    public void insert(Post p) {
//        //insert(postList, p, getAdapterItemCount());
//        postList.add(p);
//    }
//
//    public void clear() {
//        postList.clear();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    public Post getPost(int position) {
//        return postList.get(position);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder vh, int i) {
//            // Try catch required to handle instance where "load more" view is injected and cannot be cast
//            try {
//                Post p = postList.get(i);
//                Glide.with(c).load(p.getFeaturedImage().getSourceURL())
//                        .into(((ViewHolder)vh).featuredImage);
//                ((ViewHolder)vh).postTitle.setText(Html.fromHtml(p.getTitle()));
//            } catch (ClassCastException e) {
//                e.printStackTrace();
//            } catch (IndexOutOfBoundsException e) {
//
//            }
//    }
//
//    @Override
//    public int getAdapterItemCount() {
//        return postList.size();
//    }
//
//
//    @Override
//    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup viewGroup) {
//        this.c = viewGroup.getContext();
//        View itemView = LayoutInflater.from(c).inflate(R.layout.post_card_layout, viewGroup, false);
//        return new ViewHolder(itemView);
//    }
//
//    class ViewHolder extends UltimateRecyclerviewViewHolder {
//        @InjectView(R.id.post_card) protected CardView postCard;
//        @InjectView(R.id.post_card_layout_post_title) protected TextView postTitle;
//        @InjectView(R.id.post_card_layout_featured_image) protected ImageView featuredImage;
//
//        public ViewHolder(View v) {
//            super(v);
//            ButterKnife.inject(this, v);
//        }
//
//    }

}
