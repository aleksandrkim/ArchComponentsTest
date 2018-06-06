package aleksandrkim.ArchComponentsTest.HostActivity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Aleksandr Kim on 30 May, 2018 11:16 AM for ArchComponentsTest
 */

public interface NavigationActivity {

    void setTitle(int stringId);

    void setUpButton(boolean state);

    void launchWholeFragment(@NonNull Fragment fragment, @Nullable String tag);

    void finish();

    interface BackEnabled {
        void onBackPressed(); // to handle back button in fragments' methods
    }

}
