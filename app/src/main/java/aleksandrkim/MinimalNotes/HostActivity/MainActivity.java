package aleksandrkim.MinimalNotes.HostActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import aleksandrkim.MinimalNotes.NoteFeed.NotesFeedFragment;
import aleksandrkim.MinimalNotes.R;

public class MainActivity extends AppCompatActivity implements NavigationActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
//            launchWholeFragment(NotesFeedFragment.newInstance(), NotesFeedFragment.TAG);
            launchInitialFragment(NotesFeedFragment.newInstance(), NotesFeedFragment.TAG);
        }
    }

    @Override
    public void launchWholeFragment(@NonNull Fragment fragment, @Nullable String tag) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    public void launchInitialFragment(@NonNull Fragment fragment, @Nullable String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
        if (currentFragment instanceof BackEnabled) {
            ((BackEnabled) currentFragment).onBackPressed();
        }

        super.onBackPressed();
    }

    @Override
    public void setUpButton(boolean state) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(state);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
