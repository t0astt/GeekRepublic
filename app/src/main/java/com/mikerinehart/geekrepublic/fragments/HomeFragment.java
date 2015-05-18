package com.mikerinehart.geekrepublic.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
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

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.RestClient;
import com.mikerinehart.geekrepublic.activities.ArticleActivity;
import com.mikerinehart.geekrepublic.adapters.PostAdapter;
import com.mikerinehart.geekrepublic.interfaces.ApiService;
import com.mikerinehart.geekrepublic.models.Post;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeFragment extends Fragment {
    @InjectView(R.id.home_recyclerview) UltimateRecyclerView mUltimateRecyclerView;
    LinearLayoutManager mLayoutManager;
    PostAdapter mAdapter;
    RestClient mRestClient;
    ApiService mApiService;
    int mPageNumber = 1;

    private OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestClient = new RestClient();
        mApiService = mRestClient.getApiService();
        mAdapter = new PostAdapter();
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);

        mLayoutManager = new LinearLayoutManager(mUltimateRecyclerView.getContext());
        mUltimateRecyclerView.setLayoutManager(mLayoutManager);
        mUltimateRecyclerView.enableLoadmore();
        mUltimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                getMorePosts();
            }
        });
        mAdapter.setCustomLoadMoreView(LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.view_more_progress, null));
        mUltimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
                mUltimateRecyclerView.setRefreshing(false);
            }
        });

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        mUltimateRecyclerView.mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                ViewGroup child = (ViewGroup)rv.findChildViewUnder(e.getX(), e.getY());

                if (child != null && mGestureDetector.onTouchEvent(e)) {
                    int itemClicked = rv.getChildPosition(child);

                    Post p = mAdapter.getPost(itemClicked);
                    Intent intent = new Intent(getActivity(), ArticleActivity.class);
                    intent.putExtra("articleTitle", p.getTitle());
                    intent.putExtra("articleContent", p.getContent());
                    intent.putExtra("articleFeaturedImageURL", p.getFeaturedImage().getSourceURL());
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });

        getPosts();

        return view;
    }

    private void getPosts() {
        mApiService.getNews(new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {


                mUltimateRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
                mPageNumber++;
                for (int i = 0; i < posts.size(); i++) {
                    mAdapter.add(i, posts.get(i));
                }
                Log.i("Shit", "TotalItemCount" + mLayoutManager.getItemCount());
                Log.i("Shit", "Last visible item position" + mLayoutManager.findLastVisibleItemPosition());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Failed to retrieve posts", Toast.LENGTH_SHORT);
            }
        });
    }

    private void getMorePosts() {
        mApiService.getMoreNews(mPageNumber, new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                mPageNumber++;
                int positionOffset = mAdapter.getItemCount();
                for (int i = 0; i < posts.size(); i++) {
                    mAdapter.add(positionOffset + i, posts.get(i));
                }
                Log.i("Shit", "TotalItemCount" + mLayoutManager.getItemCount());
                Log.i("Shit", "Last visible item position" + mLayoutManager.findLastVisibleItemPosition());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Failed to retrieve posts", Toast.LENGTH_SHORT);
            }
        });
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
