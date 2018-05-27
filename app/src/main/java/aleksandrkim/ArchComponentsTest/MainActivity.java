package aleksandrkim.ArchComponentsTest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import aleksandrkim.ArchComponentsTest.NoteFeed.NotesFeedFragment;

public class MainActivity extends AppCompatActivity {

    private static final String FEED_FRAGMENT_TAG = "FeedFragmentTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            launchFeedFragment();
        }
    }

    private void launchFeedFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, new NotesFeedFragment(), FEED_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    public interface BackEnabled {
        void onBackPressed(); // to handle back button in fragments' methods
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2)
            finish();

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_frame);
        if (currentFragment instanceof BackEnabled) {
            ((BackEnabled) currentFragment).onBackPressed();
        }

        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
