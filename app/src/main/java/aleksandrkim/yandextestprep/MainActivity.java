package aleksandrkim.yandextestprep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import aleksandrkim.yandextestprep.Db.NotesFeedVM;
import aleksandrkim.yandextestprep.NoteFeed.NotesFeedFragment;

public class MainActivity extends AppCompatActivity {

    private static final String FEED_FRAGMENT_TAG = "FeedFragmentTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProviders.of(this).get(NotesFeedVM.class);

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

    @Override
    public void onBackPressed() {
        Log.i("MainActivity", "onBackPressed: " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() < 2)
            finish();
        super.onBackPressed();
    }
}
