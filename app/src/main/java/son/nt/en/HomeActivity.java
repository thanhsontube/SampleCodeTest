package son.nt.en;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;

import com.bumptech.glide.Glide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import son.nt.en.base.BaseActivity;
import son.nt.en.base.BaseFragment;
import son.nt.en.chat.ChatFragment;
import son.nt.en.debug.DebugActivity;
import son.nt.en.elite.EliteDailyActivity;
import son.nt.en.esl.EslDaiLyFragment;
import son.nt.en.feed.FeedFragment;
import son.nt.en.google_client_api.DaggerGoogleApiComponent;
import son.nt.en.google_client_api.GoogleApiClientModule;
import son.nt.en.hellochao.HelloChaoFragment;
import son.nt.en.home.HomeAdapter;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,  GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = HomeActivity.class.getSimpleName();
    private HomeAdapter         mHomeAdapter;
    private ViewPager           mViewPager;

//    @BindView(R.id.nav_name)
    TextView mTxtName;
    ImageView mImgHeader;

    @Inject
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        GoogleApiClientModule googleApiClientModule = new GoogleApiClientModule(this, getString(R.string.default_web_client_id), this);
        DaggerGoogleApiComponent.builder().googleApiClientModule(googleApiClientModule).build().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        mTxtName = (TextView) header.findViewById(R.id.nav_name);
        mImgHeader = (ImageView) header.findViewById(R.id.nav_header_imageView);

        if (mFirebaseUser != null)
        {
            mTxtName.setText(mFirebaseUser.getDisplayName());
            Glide.with(this).load(mFirebaseUser.getPhotoUrl()).into(mImgHeader);
        }


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager()
    {

        mHomeAdapter = new HomeAdapter(getSupportFragmentManager(), new ArrayList<BaseFragment>());
        mViewPager.setAdapter(mHomeAdapter);

        mHomeAdapter.addFragment(FeedFragment.newInstance());


        mHomeAdapter.addFragment(EslDaiLyFragment.newInstace());
        mHomeAdapter.addFragment(HelloChaoFragment.newInstace());
        mHomeAdapter.addFragment(ChatFragment.newInstance());
        //        mHomeAdapter.addFragment (HelloChaoFragment.newInstace());

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem menuItem = menu.add(0,10,0,"Search");
        menuItem.setIcon(R.drawable.ic_gf_search);
        menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            startActivity(new Intent(this, DebugActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        }
        else if (id == R.id.nav_gallery)
        {
            startActivity(new Intent(this, EliteDailyActivity.class));

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
