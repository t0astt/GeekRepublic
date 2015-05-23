package com.mikerinehart.geekrepublic.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikerinehart.geekrepublic.Constants;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.fragments.ArticleListFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements
        ArticleListFragment.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences favoriteArticleSharedPreferences;

    @InjectView(R.id.toolbar) public Toolbar toolbar;

    private int mFavoriteArticleCount = 0;
    private Drawer.Result mDrawer;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Log.i("MainActivity SP", "SharedPreferenceChanged Fired");
        mDrawer.updateBadge(Integer.toString(getFavoriteArticleCount()), 8);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        favoriteArticleSharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES_FAVORITE_ARTICLE, Context.MODE_PRIVATE);
        favoriteArticleSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        mDrawer = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(R.string.navigation_drawer_home)
                                .withName(R.string.navigation_drawer_home)
                                .withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem()
                                .withIdentifier(R.string.navigation_drawer_news)
                                .withName(R.string.navigation_drawer_news)
                                .withIcon(FontAwesome.Icon.faw_newspaper_o),
                        new PrimaryDrawerItem()
                                .withIdentifier(R.string.navigation_drawer_security)
                                .withName(R.string.navigation_drawer_security)
                                .withIcon(FontAwesome.Icon.faw_lock),
                        new PrimaryDrawerItem()
                                .withIdentifier(R.string.navigation_drawer_gaming)
                                .withName(R.string.navigation_drawer_gaming)
                                .withIcon(FontAwesome.Icon.faw_gamepad),
                        new PrimaryDrawerItem()
                                .withIdentifier(R.string.navigation_drawer_mobile)
                                .withName(R.string.navigation_drawer_mobile)
                                .withIcon(FontAwesome.Icon.faw_mobile_phone),
                        new PrimaryDrawerItem()
                                .withIdentifier(R.string.navigation_drawer_technology)
                                .withName(R.string.navigation_drawer_technology)
                                .withIcon(FontAwesome.Icon.faw_desktop),
                        new PrimaryDrawerItem()
                                .withIdentifier(R.string.navigation_drawer_culture)
                                .withName(R.string.navigation_drawer_culture)
                                .withIcon(FontAwesome.Icon.faw_users),
                        new PrimaryDrawerItem()
                                .withIdentifier(R.string.navigation_drawer_gadgets)
                                .withName(R.string.navigation_drawer_gadgets)
                                .withIcon(FontAwesome.Icon.faw_headphones),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_favorites)
                                .withIdentifier(R.string.navigation_drawer_favorites)
                                .withIcon(FontAwesome.Icon.faw_heart)
                                .withBadge(Integer.toString(getFavoriteArticleCount())),
                        new SectionDrawerItem().withName(R.string.navigation_drawer_more),
                        new SecondaryDrawerItem().withName(R.string.navigation_drawer_facebook).withIcon(FontAwesome.Icon.faw_facebook_official),
                        new SecondaryDrawerItem().withName(R.string.navigation_drawer_twitter).withIcon(FontAwesome.Icon.faw_twitter),
                        new SecondaryDrawerItem().withName(R.string.navigation_drawer_contactus).withIcon(FontAwesome.Icon.faw_envelope)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(getApplicationContext(), MainActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show(); // TODO: For debugging purposes
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            switch (position) {
                                // Home
                                case 0:
                                    ft.replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_HOME));
                                    ft.addToBackStack("Home");
                                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                    ft.commit();
                                    break;
                                // News
                                case 1:
                                    ft.replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_NEWS));
                                    ft.addToBackStack("News");
                                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                    ft.commit();
                                    break;
                                // Security
                                case 2:
                                    ft.replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_SECURITY));
                                    ft.addToBackStack("Security");
                                    ft.commit();
                                    break;
                                // Gaming
                                case 3:
                                    ft.replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_GAMING));
                                    ft.addToBackStack("Gaming");
                                    ft.commit();
                                    break;
                                // Mobile
                                case 4:
                                    ft.replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_MOBILE));
                                    ft.addToBackStack("Mobile");
                                    ft.commit();
                                    break;
                                // Technology
                                case 5:
                                    ft.replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_TECHNOLOGY));
                                    ft.addToBackStack("Technology");
                                    ft.commit();
                                    break;
                                // Culture
                                case 6:
                                    ft.replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_CULTURE));
                                    ft.addToBackStack("Culture");
                                    ft.commit();
                                    break;
                                // Gadgets
                                case 7:
                                    ft.replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_GADGETS));
                                    ft.addToBackStack("Gadgets");
                                    ft.commit();
                                    break;
                                case 8:
                                    ft.replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_FAVORITES));
                                    ft.addToBackStack("Favorites");
                                    ft.commit();
                                    break;
                                // Facebook
                                case 10:
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse(Constants.SOCIAL_FACEBOOK_APP_URI));
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse(Constants.SOCIAL_FACEBOOK_BROWSER_URI)));
                                    }
                                    break;
                                // Twitter
                                case 11:
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse(Constants.SOCIAL_TWITTER_APP_URI));
                                        startActivity(intent);

                                    } catch (ActivityNotFoundException e) {
                                        startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse(Constants.SOCIAL_TWITTER_BROWSER_URI)));
                                    }
                                    break;
                                // Contact Us
                                case 12:
                                    break;
                            }
                        }
                    }
                })
                .build();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getFragmentManager().beginTransaction()
                        .replace(R.id.layout_container, ArticleListFragment.newInstance(Constants.CATEGORY_HOME))
                        .commit();
            }
        });
    }

    private int getFavoriteArticleCount() {
        return favoriteArticleSharedPreferences.getAll().size();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onFragmentInteraction(Uri uri) {}
}
