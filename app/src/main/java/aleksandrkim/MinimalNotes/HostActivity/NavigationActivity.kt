package aleksandrkim.MinimalNotes.HostActivity

import android.support.v4.app.Fragment

/**
 * Created by Aleksandr Kim on 30 May, 2018 11:16 AM for ArchComponentsTest
 */

interface NavigationActivity {

    fun setTitle(stringId: Int)

    fun setUpButton(state: Boolean)

    fun launchWholeFragment(fragment: Fragment, tag: String?)

    fun finish()

    interface BackEnabled {
        fun onBackPressed()  // to handle back button in fragments' methods
    }

}