package com.mikerinehart.geekrepublic.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.gson.Gson;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.manuelpeinado.fadingactionbar.view.ObservableScrollable;
import com.manuelpeinado.fadingactionbar.view.OnScrollChangedCallback;
import com.mikerinehart.geekrepublic.Constants;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.models.Post;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArticleActivity extends AppCompatActivity implements
        OnScrollChangedCallback, ShareActionProvider.OnShareTargetSelectedListener {

    Intent mIntent;
    Post article;
    Gson gson;

    private int mLastDampedScroll;
    private int mInitialStatusBarColor;

    SharedPreferences favoriteArticleSharedPreferences;
    SharedPreferences.Editor favoriteArticleSharedPreferencesEditor;

    private ShareActionProvider mShareActionProvider = null;
    private Intent mShareIntent;

    private AdRequest mAdRequest;

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.article_title) TextView mArticleTitleTextView;
    @InjectView(R.id.article_content) TextView mArticleContentTextView;
    @InjectView(R.id.article_author) TextView mArticleAuthorTextView;
    @InjectView(R.id.article_publish_date) TextView mArticlePublishDate;
    @InjectView(R.id.header) ImageView mArticleHeader;
    @InjectView(R.id.article_scrollview) ObservableScrollable mScrollView;
    @InjectView(R.id.article_adview_container) FrameLayout mAdviewContainer;
    @InjectView(R.id.article_adview_close) ImageView mAdviewCloseButton;
    @InjectView(R.id.article_adview) AdView mAdview;

    Drawable mActionBarBackgroundDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.inject(this);

        mAdRequest = new AdRequest.Builder().build();
        mAdview.loadAd(mAdRequest);
        mAdview.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdviewContainer.setVisibility(FrameLayout.VISIBLE);
            }
        });
        mAdviewCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdview.destroy();
                mAdviewContainer.setVisibility(FrameLayout.GONE);
            }
        });

        mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("text/plain");

        favoriteArticleSharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES_FAVORITE_ARTICLE, Context.MODE_PRIVATE);
        favoriteArticleSharedPreferencesEditor = favoriteArticleSharedPreferences.edit();
        gson = new Gson();

        mActionBarBackgroundDrawable = mToolbar.getBackground();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mScrollView.setOnScrollChangedCallback(this);
        onScroll(-1, 0);

        mIntent = getIntent();
        article = gson.fromJson(mIntent.getStringExtra("post"), Post.class); // Un-serialize JSON into Post object
        mShareIntent.putExtra(Intent.EXTRA_TEXT, article.getUrl());


        SimpleDateFormat df = new SimpleDateFormat("MMMM d', 'yyyy");

        Glide.with(mArticleHeader.getContext()).load(article.getFeaturedImage().getSourceURL())
                .into(mArticleHeader);
        mArticleTitleTextView.setText(Html.fromHtml(article.getTitle()));
        mArticleContentTextView.setText(Html.fromHtml(article.getContent()));
        mArticleAuthorTextView.setText("By: " + Html.fromHtml(article.getAuthor().getName()));
        mArticlePublishDate.setText("Published: " + df.format(article.getDateCreated()));

    }

    @Override
    public void onScroll(int l, int scrollPosition) {
        int headerHeight = mArticleHeader.getHeight() - mToolbar.getHeight();
        float ratio = 0;
        if (scrollPosition > 0 && headerHeight > 0) {
            ratio = (float)Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;
        }

        updateActionBarTransparency(ratio);
//        updateStatusBarColor(ratio);
        updateParallaxEffect(scrollPosition);
    }

    private void updateActionBarTransparency(float scrollRatio) {
        int newAlpha = (int) (scrollRatio * 255);
        mActionBarBackgroundDrawable.setAlpha(newAlpha);
        mToolbar.setBackgroundDrawable(mActionBarBackgroundDrawable);
    }

//    private void updateStatusBarColor(float scrollRatio) {
//        int r = interpolate(Color.red(mInitialStatusBarColor), Color.red(mFinalStatusBarColor), 1 - scrollRatio);
//        int g = interpolate(Color.green(mInitialStatusBarColor), Color.green(mFinalStatusBarColor), 1 - scrollRatio);
//        int b = interpolate(Color.blue(mInitialStatusBarColor), Color.blue(mFinalStatusBarColor), 1 - scrollRatio);
//        mStatusBarManager.setTintColor(Color.rgb(r, g, b));
//    }

    private void updateParallaxEffect(int scrollPosition) {
        float damping = 0.16f; // original is 0.5f, higher number results in less parallax
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        mArticleHeader.offsetTopAndBottom(-offset);

        mLastDampedScroll = dampedScroll;
    }

    private int interpolate(int from, int to, float param) {
        return (int) (from * param + to * (1 - param));
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        return(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        MenuItem favoriteArticleMenuItem = menu.findItem(R.id.menu_item_favorite);
        MenuItem shareArticleMenuItem = menu.findItem(R.id.menu_item_share);
        shareArticleMenuItem.setEnabled(true);

        mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareArticleMenuItem);
        mShareActionProvider.setOnShareTargetSelectedListener(this);
        mShareActionProvider.setShareIntent(mShareIntent);

        // If article is found in SP, then it is favorited so set it checked
        if (favoriteArticleSharedPreferences.contains(Integer.toString(article.getId()))) {
            favoriteArticleMenuItem.setChecked(true);
            favoriteArticleMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
        }
        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_favorite:
                if (!item.isChecked()) {
                    // Favorite article
                    item.setChecked(true);

                    favoriteArticleSharedPreferencesEditor.putString(Integer.toString(article.getId()), gson.toJson(article));
                    favoriteArticleSharedPreferencesEditor.apply();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                    Toast.makeText(getApplicationContext(), "Article Favorited!", Toast.LENGTH_SHORT).show();

                } else {
                    // Unfavorite article
                    item.setChecked(false);
                    favoriteArticleSharedPreferencesEditor.remove(Integer.toString(article.getId()));
                    favoriteArticleSharedPreferencesEditor.apply();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_outline));
                    Toast.makeText(getApplicationContext(), "Article Unfavorited!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}