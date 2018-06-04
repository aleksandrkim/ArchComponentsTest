package aleksandrkim.ArchComponentsTest.HostActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import aleksandrkim.ArchComponentsTest.NoteFeed.NotesFeedFragment;
import aleksandrkim.ArchComponentsTest.R;

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
            launchWholeFragment(NotesFeedFragment.newInstance(), NotesFeedFragment.TAG);
        }
    }

    @Override
    public void launchWholeFragment(@NonNull Fragment fragment, @Nullable String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

//    @Override
//    public void showSnackbar(int displayTextId, int duration, int actionTextId, View.OnClickListener onActionClicked, Runnable
//            onDismissed) {
//        Snackbar.make(findViewById(R.id.coordinator), displayTextId, duration)
//                .setAction(actionTextId, onActionClicked)
//                .addCallback(new Snackbar.Callback() {
//                    @Override
//                    public void onDismissed(Snackbar transientBottomBar, int event) {
//                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION)
//                            onDismissed.run();
//                        super.onDismissed(transientBottomBar, event);
//                    }
//                }).show();
//    }

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
    public void setUpButton(boolean state) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(state);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
