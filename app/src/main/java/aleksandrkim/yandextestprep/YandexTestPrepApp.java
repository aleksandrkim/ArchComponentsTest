package aleksandrkim.yandextestprep;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Aleksandr Kim on 08 Apr, 2018 12:06 AM for YandexTestPrep
 */

public class YandexTestPrepApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder()
                        .schemaVersion(BuildConfig.VERSION_CODE)
                        .build());
    }
}
