package com.mikerinehart.geekrepublic.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.RestClient;
import com.mikerinehart.geekrepublic.activities.ArticleActivity;
import com.mikerinehart.geekrepublic.adapters.ArticleAdapter;
import com.mikerinehart.geekrepublic.interfaces.ApiService;
import com.mikerinehart.geekrepublic.models.Post;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArticleListFragment extends Fragment {
    @InjectView(R.id.home_recyclerview) UltimateRecyclerView ultimateRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArticleAdapter mAdapter;
    private ScaleInAnimationAdapter mAnimationAdapter;
    private RestClient mRestClient;
    private ApiService mApiService;


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

        mRestClient = new RestClient();
        mApiService = mRestClient.getApiService();
        mAdapter = new ArticleAdapter();
        mAnimationAdapter = new ScaleInAnimationAdapter(mAdapter);
        getArticles();
        Log.i("ArticleListFragment", "Running onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ArticleListFragment", "Running onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);

        ultimateRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
        mLayoutManager = new LinearLayoutManager(ultimateRecyclerView.getContext());
        ultimateRecyclerView.setLayoutManager(mLayoutManager);
        ultimateRecyclerView.enableLoadmore();
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                mPageNumber++;
                getArticles();
            }
        });
        mAdapter.setCustomLoadMoreView(inflater.inflate(R.layout.view_more_progress, null));
        ultimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNumber = 1;
                mAdapter.clear();
                getArticles();
            }
        });

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

                    Post p = mAdapter.getPost(itemClicked);
                    Intent intent = new Intent(getActivity(), ArticleActivity.class);
                    intent.putExtra("articleTitle", p.getTitle());
                    intent.putExtra("articleContent", p.getContent());
                    intent.putExtra("articleFeaturedImageURL", p.getFeaturedImage().getSourceURL());
                    intent.putExtra("articleAuthor", p.getAuthor().getName());
                    intent.putExtra("articlePublishDate", p.getDateCreated().getTime());
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });

        return view;
    }

    /**
     * Fetches articles based on mCategory variable.
     */
    private void getArticles() {
        Callback<List<Post>> callback = new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                //ultimateRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
                if (ultimateRecyclerView.getAdapter() == null) {
                    ultimateRecyclerView.setAdapter(mAnimationAdapter);
                }
                for (int i = 0; i < posts.size(); i++) {
                    mAdapter.insert(posts.get(i));
                }

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
        }

//        mApiService.getNews(new Callback<List<Post>>() {
//            @Override
//            public void success(List<Post> posts, Response response) {
//
//                ultimateRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
//                mAdapter.clear();
//                mPageNumber = 1;
//                for (int i = 0; i < posts.size(); i++) {
//                    mAdapter.add(i, posts.get(i));
//                }
//
//                ultimateRecyclerView.setRefreshing(false);
//                Log.i("Shit", "TotalItemCount" + mLayoutManager.getItemCount());
//                Log.i("Shit", "Last visible item position" + mLayoutManager.findLastVisibleItemPosition());
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Toast.makeText(getActivity(), "Failed to retrieve posts", Toast.LENGTH_SHORT).show();
//                ultimateRecyclerView.setRefreshing(false);
//            }
//        });
    }

//    private void getMorePosts() {
//        mApiService.getMoreNews(mPageNumber, new Callback<List<Post>>() {
//            @Override
//            public void success(List<Post> posts, Response response) {
//                mPageNumber++;
//                int positionOffset = mAdapter.getItemCount();
//                for (int i = 0; i < posts.size(); i++) {
//                    mAdapter.add(positionOffset + i, posts.get(i));
//                }
//                Log.i("Shit", "TotalItemCount" + mLayoutManager.getItemCount());
//                Log.i("Shit", "Last visible item position" + mLayoutManager.findLastVisibleItemPosition());
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Toast.makeText(getActivity(), "Failed to retrieve posts", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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
