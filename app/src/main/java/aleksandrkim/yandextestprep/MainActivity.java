package aleksandrkim.yandextestprep;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import aleksandrkim.yandextestprep.NoteFeed.NotesFeedFragment;

public class MainActivity extends AppCompatActivity {

    private static final String FEED_FRAGMENT_TAG = "FeedFragmentTag";

    NotesFeedFragment feedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            feedFragment = new NotesFeedFragment();
            launchFragment();
        }
    }

    private void launchFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, feedFragment, FEED_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2)
            finish();
        super.onBackPressed();
    }
}
