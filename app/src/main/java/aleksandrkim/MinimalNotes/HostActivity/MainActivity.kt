package aleksandrkim.MinimalNotes.HostActivity

import aleksandrkim.MinimalNotes.NoteFeed.NotesFeedFragment
import aleksandrkim.MinimalNotes.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null)
            launchInitialFragment(NotesFeedFragment.newInstance(), NotesFeedFragment.TAG)
    }

    override fun launchWholeFragment(fragment: Fragment, tag: String?) {
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_frame)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame, fragment, tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null).commit()
    }

    private fun launchInitialFragment(fragment: Fragment, tag: String?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame, fragment, tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_frame)
        if (currentFragment is NavigationActivity.BackEnabled) currentFragment.onBackPressed()
        super.onBackPressed()
    }

    override fun setUpButton(state: Boolean) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(state)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
