package fitnessgods.udacity.com.fitnessgods;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import fitnessgods.udacity.com.fitnessgods.Fragments.AboutUsFragment;
import fitnessgods.udacity.com.fitnessgods.Fragments.CustomListFragment;
import fitnessgods.udacity.com.fitnessgods.Fragments.WorkoutsFragment;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    //This is our viewPager
    private ViewPager viewPager;
    MenuItem prevMenuItem;
    WorkoutsFragment WorkoutsFragment;
    CustomListFragment CustomListFragment;
    AboutUsFragment aboutUsFragment;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String loggedInby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();
        mFirebaseAuth =  FirebaseAuth.getInstance();
        Intent intent = getIntent();
        loggedInby = intent.getStringExtra("Login");

        if(loggedInby.equals("Google"))
        {
            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    //In case we are logged out to redirected to the Login page
                    if(firebaseAuth.getCurrentUser() == null)
                    {
                        startActivity(new Intent(MainActivity.this , LoginActivity.class));
                    }
                }
            };
        }

        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //toolbar.setTitle("Fitness Gods");
        setupViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                if(loggedInby.equals("Google"))
                    mFirebaseAuth.signOut();
                else if (loggedInby.equals("Facebook"))
                    LoginManager.getInstance().logOut();

                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_main:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.action_list:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.action_about:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return false;
        }
    };

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        WorkoutsFragment=new WorkoutsFragment();
        CustomListFragment=new CustomListFragment();
        aboutUsFragment=new AboutUsFragment();
        adapter.addFragment(WorkoutsFragment);
        adapter.addFragment(CustomListFragment);
        adapter.addFragment(aboutUsFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(loggedInby.equals("Google"))
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loggedInby.equals("Google"))
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
