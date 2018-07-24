package fitnessgods.udacity.com.fitnessgods;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private String loggedInby;
    boolean isLoggedInGoogle , isLoggedInFacebook;
    CallbackManager callbackManager;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();
        mFirebaseAuth =  FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedInFacebook = accessToken != null && !accessToken.isExpired();

        if(currentUser != null)
        {
            isLoggedInGoogle = true;
            loggedInby = "Google";
        }

        if(isLoggedInFacebook)
            loggedInby ="Facebook";

        if(savedInstanceState != null)
        {
            loggedInby =  savedInstanceState.getString("Login");
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loggedInby = "Facebook";
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        if(!isLoggedInGoogle && !isLoggedInFacebook)
        {
              startActivity(new Intent(MainActivity.this , LoginActivity.class));
        }
        else
        {
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
                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                    prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            setupViewPager(viewPager);
        }

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
                if(currentUser != null)
                    mFirebaseAuth.signOut();
                else if (loggedInby.equals("Facebook"))
                    LoginManager.getInstance().logOut();

                startActivity(new Intent(MainActivity.this , LoginActivity.class));
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
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        //Must save the step data in case we rotate the screen
        currentState.putString("Login" , loggedInby);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
           // finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
