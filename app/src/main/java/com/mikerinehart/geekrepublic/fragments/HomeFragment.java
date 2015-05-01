package com.mikerinehart.geekrepublic.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mikerinehart.geekrepublic.Constants;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.RestClient;
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

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener {
    @InjectView(R.id.home_recyclerview) SuperRecyclerView mRecyclerView;
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
    }

    @DebugLog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);

        mLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setRefreshListener(this);
        mRecyclerView.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecyclerView.setupMoreListener(this, 1);

        getPosts();

        return view;
    }

    @Override
    public void onRefresh() {
        getPosts();
    }

    @Override
    public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
        getMorePosts();
        Log.i("HomeFragment", "Getting more posts");
    }

    private void getPosts() {
        mApiService.getNews(new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {

                mAdapter = new PostAdapter();
                mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
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
                mRecyclerView.hideMoreProgress();
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
