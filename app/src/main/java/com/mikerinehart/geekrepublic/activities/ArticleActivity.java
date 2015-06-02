package com.mikerinehart.geekrepublic.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.mikerinehart.geekrepublic.Constants;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.models.Post;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArticleActivity extends AppCompatActivity implements ShareActionProvider.OnShareTargetSelectedListener {
    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.web_view) ObservableWebView mWebView;
    @InjectView(R.id.header) View mHeaderView;
    @InjectView(R.id.featured_image) ImageView mFeaturedImage;
    @InjectView(R.id.title) TextView mArticleTitle;
    @InjectView(R.id.author) TextView mArticleAuthor;
    @InjectView(R.id.pub_date) TextView mArticleDate;
    @InjectView(R.id.article_adview_container) FrameLayout mAdviewContainer;
    @InjectView(R.id.article_adview_close) ImageView mAdviewCloseButton;
    @InjectView(R.id.article_adview) AdView mAdview;


    private int mHeaderHeight;

    private Intent mIntent;
    private Post mArticle;

    private Gson mGson;
    private SharedPreferences mFavoriteArticleSharedPreferences;
    private SharedPreferences.Editor mFavoriteArticleSharedPreferencesEditor;

    private ShareActionProvider mShareActionProvider = null;
    private Intent mShareIntent;

    private AdRequest mAdRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        mFavoriteArticleSharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES_FAVORITE_ARTICLE, Context.MODE_PRIVATE);
        mFavoriteArticleSharedPreferencesEditor = mFavoriteArticleSharedPreferences.edit();

        mGson = new Gson();
        mIntent = getIntent();
        mArticle = mGson.fromJson(mIntent.getStringExtra("post"), Post.class); // Un-serialize JSON into Post object
        mShareIntent.putExtra(Intent.EXTRA_TEXT, mArticle.getUrl());

        mHeaderView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mHeaderHeight = (bottom - top);
                applyHeaderHeightToWebViewContent();
            }
        });

        mWebView.setCallback(new ObservableWebView.Callback() {
            @Override
            public void onScrollChanged(int left, int top) {
                mHeaderView.setTranslationY(-top);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                applyHeaderHeightToWebViewContent();
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        SimpleDateFormat df = new SimpleDateFormat("MMMM d', 'yyyy");

        Glide.with(mFeaturedImage.getContext())
                .load(mArticle.getFeaturedImage().getSourceURL())
                .into(mFeaturedImage);
        mArticleTitle.setText(mArticle.getTitle());
        mArticleAuthor.setText("By: " + mArticle.getAuthor().getName());
        mArticleDate.setText("Published: " + df.format(mArticle.getDateCreated()));

        StringBuilder sb = new StringBuilder();
        //sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0,target-densityDpi=device-dpi\">");
        sb.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"utf-8\">\n" +
                "\t\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "\t\t<title>Page title</title>\n" +
                "\t</head>\n" +
                "\t<body>");
        sb.append(mArticle.getContent());
        sb.append("</body>\n" +
                "</html>");
        mWebView.loadData(sb.toString(), "text/html; charset=UTF-8", null);
    }

    private void applyHeaderHeightToWebViewContent() {
        String script = "javascript:document.body.style.marginTop='" +
                (mHeaderHeight / getResources().getDisplayMetrics().density) + "px';";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            mWebView.evaluateJavascript(script, null);
        } else {
            mWebView.loadUrl(script);
        }
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

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareArticleMenuItem);
        mShareActionProvider.setOnShareTargetSelectedListener(this);
        mShareActionProvider.setShareIntent(mShareIntent);

        // If article is found in SP, then it is favorited so set it checked
        if (mFavoriteArticleSharedPreferences.contains(Integer.toString(mArticle.getId()))) {
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

                    mFavoriteArticleSharedPreferencesEditor.putString(Integer.toString(mArticle.getId()), mGson.toJson(mArticle));
                    mFavoriteArticleSharedPreferencesEditor.apply();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                    Toast.makeText(getApplicationContext(), "Article added to favorites!", Toast.LENGTH_SHORT).show();

                } else {
                    // Unfavorite article
                    item.setChecked(false);
                    mFavoriteArticleSharedPreferencesEditor.remove(Integer.toString(mArticle.getId()));
                    mFavoriteArticleSharedPreferencesEditor.apply();
                    item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_outline));
                    Toast.makeText(getApplicationContext(), "Article removed from favorites!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class ObservableWebView extends WebView {
        private Callback mCallback;

        public ObservableWebView(Context context)
        {
            super(context);
        }

        public ObservableWebView(Context context, AttributeSet attrs)
        {
            super(context, attrs);
        }

        public void setCallback(Callback callback)
        {
            mCallback = callback;
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            if(mCallback != null) {
                mCallback.onScrollChanged(l, t);
            }
        }

        public interface Callback {
            void onScrollChanged(int left, int top);
        }
    }
}