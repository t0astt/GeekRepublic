package com.mikerinehart.geekrepublic.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.gson.Gson;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.animators.ScaleInAnimator;
import com.mikerinehart.geekrepublic.Constants;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.RestClient;
import com.mikerinehart.geekrepublic.activities.ArticleActivity;
import com.mikerinehart.geekrepublic.activities.MainActivity;
import com.mikerinehart.geekrepublic.adapters.ArticleAdapter;
import com.mikerinehart.geekrepublic.interfaces.ApiService;
import com.mikerinehart.geekrepublic.models.Post;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArticleListFragment extends Fragment {
    @InjectView(R.id.home_recyclerview) UltimateRecyclerView ultimateRecyclerView;
    @InjectView(R.id.article_list_adview_container) FrameLayout mAdviewContainer;
    @InjectView(R.id.article_list_adview_close) ImageView mAdviewCloseButton;
    @InjectView(R.id.article_list_adview) AdView mAdview;

    private LinearLayoutManager mLayoutManager;
    private ArticleAdapter mAdapter;
    private ScaleInAnimationAdapter mAnimationAdapter;
    private RestClient mRestClient;
    private ApiService mApiService;
    private AdRequest mAdRequest;

    Gson gson;
    SharedPreferences favoriteArticleSharedPreferences;

    private int mCategory = 0;
    private int mPageNumber = 1;

    private static final String ARG_CATEGORY = "category";

    private OnFragmentInteractionListener mListener;

    public ArticleListFragment() {}

    public static ArticleListFragment newInstance(int category) {
        ArticleListFragment f = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, category);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getInt(ARG_CATEGORY, 0); // Grab the category so we can switch on it to determine what articles to grab
        }

        gson = new Gson();
        favoriteArticleSharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_FAVORITE_ARTICLE, Context.MODE_PRIVATE);
        mRestClient = new RestClient();
        mApiService = mRestClient.getApiService();
        mAdRequest = new AdRequest.Builder().build();

        Log.i("ArticleListFragment", "Running onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ArticleListFragment", "Running onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setActionbarTitle();
        ButterKnife.inject(this, view);
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
        mAdapter = new ArticleAdapter();
        ultimateRecyclerView.setAdapter(mAdapter);
        //ultimateRecyclerView.setItemAnimator(new ScaleInAnimator());
        //mAnimationAdapter = new ScaleInAnimationAdapter(mAdapter);


        //ultimateRecyclerView.setAdapter(mAnimationAdapter);
        mLayoutManager = new LinearLayoutManager(ultimateRecyclerView.getContext());
        ultimateRecyclerView.setLayoutManager(mLayoutManager);
        if (mCategory != 8) {
            ultimateRecyclerView.enableLoadmore();
            ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
                @Override
                public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                    mPageNumber++;
                    getArticles();
                }
            });
            mAdapter.setCustomLoadMoreView(LayoutInflater.from(ultimateRecyclerView.getContext()).inflate(R.layout.view_more_progress, null));
            ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPageNumber = 1;
                    mAdapter.clear();
                    getArticles();
                }
            });
        }


        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        ultimateRecyclerView.mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                ViewGroup child = (ViewGroup) rv.findChildViewUnder(e.getX(), e.getY());

                if (child != null && mGestureDetector.onTouchEvent(e)) {
                    int itemClicked = rv.getChildPosition(child);

                    Post p = null;
                    try {
                        p = mAdapter.getItem(itemClicked);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    String postJson = gson.toJson(p);
                    Intent intent = new Intent(getActivity(), ArticleActivity.class);
                    intent.putExtra("post", postJson);
                    Bundle b = new Bundle();
                    b.putString("post", postJson);
                    startActivity(intent, b); // TODO: Find more compatible method
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });
        getArticles();
        return view;
    }

    /**
     * Fetches articles based on mCategory variable.
     */
    private void getArticles() {
        Callback<List<Post>> callback = new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                ultimateRecyclerView.setRefreshing(false);
                displayArticles(posts);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Failed to fetch articles. Please try again!", Toast.LENGTH_SHORT).show();
                ultimateRecyclerView.setRefreshing(false);
            }
        };

        switch (mCategory) {
            case 0:
                mApiService.getAllArticles(mPageNumber, callback);
                break;
            case 1:
                mApiService.getNewsArticles(mPageNumber, callback);
                break;
            case 2:
                mApiService.getSecurityArticles(mPageNumber, callback);
                break;
            case 3:
                mApiService.getGamingArticles(mPageNumber, callback);
                break;
            case 4:
                mApiService.getMobileArticles(mPageNumber, callback);
                break;
            case 5:
                mApiService.getTechnologyArticles(mPageNumber, callback);
                break;
            case 6:
                mApiService.getCultureArticles(mPageNumber, callback);
                break;
            case 7:
                mApiService.getGadgetsArticles(mPageNumber, callback);
                break;
            case 8:
                Map<String, ?> map = favoriteArticleSharedPreferences.getAll();
                ArrayList<Post> articleList = new ArrayList<>();
                for (Map.Entry<String, ?> entry : map.entrySet()) {
                    String test = entry.getValue().toString();
                    Post p = gson.fromJson(test, Post.class);
                    articleList.add(p);
                }
                displayArticles(articleList);
                break;
        }
    }

    /*
     * Takes a list of articles and iterates through them, inserting them into the adapter
     */
    private void displayArticles(List<Post> articles) {
        for (int i = 0; i < articles.size(); i++) {
            mAdapter.insert(articles.get(i), mAdapter.getAdapterItemCount());
            //mAnimationAdapter.notifyDataSetChanged();
        }
        mAdapter.notifyDataSetChanged();
//        mAnimationAdapter.notifyDataSetChanged();
    }

    /*
     * Sets actionbar title of the fragment
     * Switches on mCategory int
     */
    private void setActionbarTitle() {
        switch (mCategory) {
            // Home
            case 0:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
                break;
            // News
            case 1:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("News");
                break;
            // Security
            case 2:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Security");
                break;
            // Gaming
            case 3:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Gaming");
                break;
            // Mobile
            case 4:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mobile");
                break;
            // Technology
            case 5:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Technology");
                break;
            // Culture
            case 6:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Culture");
                break;
            // Gadgets
            case 7:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Gadgets");
                break;
            // Favorites
            case 8:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favorites");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPageNumber = 1;
        // If resuming the fragment from a favorited article view, we should check to see if it's still a favorited article
        // Clear the adapter and grab new ones, otherwise we might display duplicates
        if (mCategory == 8) {
            mAdapter.clear();
            getArticles();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {}

}
