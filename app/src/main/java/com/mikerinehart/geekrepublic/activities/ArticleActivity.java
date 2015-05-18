package com.mikerinehart.geekrepublic.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;
import com.manuelpeinado.fadingactionbar.view.ObservableScrollable;
import com.manuelpeinado.fadingactionbar.view.OnScrollChangedCallback;
import com.mikerinehart.geekrepublic.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArticleActivity extends ActionBarActivity implements OnScrollChangedCallback {

    Intent mIntent;
    String articleTitle;
    String articleContent;
    String articleFeaturedImageURL;
    String articleAuthor;
    Date articlePublishDate;

    private int mLastDampedScroll;
    private int mInitialStatusBarColor;

    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.article_title) TextView mArticleTitleTextView;
    @InjectView(R.id.article_content) TextView mArticleContentTextView;
    @InjectView(R.id.article_author) TextView mArticleAuthorTextView;
    @InjectView(R.id.article_publish_date) TextView mArticlePublishDate;
    @InjectView(R.id.header) ImageView mArticleHeader;
    @InjectView(R.id.article_scrollview) ObservableScrollable mScrollView;
    Drawable mActionBarBackgroundDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.inject(this);

        mActionBarBackgroundDrawable = mToolbar.getBackground();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mScrollView.setOnScrollChangedCallback(this);

        onScroll(-1, 0);

        mIntent = getIntent();
        articleTitle = mIntent.getStringExtra("articleTitle");
        articleContent = mIntent.getStringExtra("articleContent");
        articleFeaturedImageURL = mIntent.getStringExtra("articleFeaturedImageURL");
        articleAuthor = mIntent.getStringExtra("articleAuthor");

        SimpleDateFormat df = new SimpleDateFormat("MMMM F', 'yyyy");
        articlePublishDate = new Date(mIntent.getLongExtra("articlePublishDate", 5L));

        getSupportActionBar().setTitle(Html.fromHtml(articleTitle));

        Glide.with(mArticleHeader.getContext()).load(articleFeaturedImageURL)
                .into(mArticleHeader);
        mArticleTitleTextView.setText(Html.fromHtml(articleTitle));
        mArticleContentTextView.setText(Html.fromHtml(articleContent));
        mArticleAuthorTextView.setText("By: " + Html.fromHtml(articleAuthor));
        mArticlePublishDate.setText("Published: " + df.format(articlePublishDate));

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
        //mToolbar.setBackground(mActionBarBackgroundDrawable); // This method call requires API 16, min is 15
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}