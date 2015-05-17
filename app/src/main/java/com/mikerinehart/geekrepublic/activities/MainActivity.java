package com.mikerinehart.geekrepublic.activities;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikerinehart.geekrepublic.R;
import com.mikerinehart.geekrepublic.fragments.HomeFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {
    @InjectView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        Drawer.Result result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_home).withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_news).withIcon(FontAwesome.Icon.faw_newspaper_o),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_security).withIcon(FontAwesome.Icon.faw_lock),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_gaming).withIcon(FontAwesome.Icon.faw_gamepad),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_mobile).withIcon(FontAwesome.Icon.faw_mobile_phone),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_technology).withIcon(FontAwesome.Icon.faw_desktop),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_culture).withIcon(FontAwesome.Icon.faw_users),
                        new PrimaryDrawerItem().withName(R.string.navigation_drawer_gadgets).withIcon(FontAwesome.Icon.faw_headphones),
                        new SectionDrawerItem().withName(R.string.navigation_drawer_more),
                        new SecondaryDrawerItem().withName(R.string.navigation_drawer_facebook).withIcon(FontAwesome.Icon.faw_facebook_official),
                        new SecondaryDrawerItem().withName(R.string.navigation_drawer_twitter).withIcon(FontAwesome.Icon.faw_twitter),
                        new SecondaryDrawerItem().withName(R.string.navigation_drawer_googleplus).withIcon(FontAwesome.Icon.faw_google_plus),
                        new SecondaryDrawerItem().withName(R.string.navigation_drawer_contactus).withIcon(FontAwesome.Icon.faw_envelope)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(getApplicationContext(), MainActivity.this.getString(((Nameable)drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                            Log.i("Drawer", position + "");
                            switch (position) {
                                // Home
                                case 0:
                                    break;
                                // News
                                case 1:
                                    break;
                                // Security
                                case 2:
                                    break;
                                // Gaming
                                case 3:
                                    break;
                                // Mobile
                                case 4:
                                    break;
                                // Technology
                                case 5:
                                    break;
                                // Culture
                                case 6:
                                    break;
                                // Gadgets
                                case 7:
                                    break;
                                // Facebook
                                case 9:
                                    break;
                                // Twitter
                                case 10:
                                    break;
                                // Google+
                                case 11:
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
                        .replace(R.id.layout_container, new HomeFragment())
                        .commit();
            }
        });
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
