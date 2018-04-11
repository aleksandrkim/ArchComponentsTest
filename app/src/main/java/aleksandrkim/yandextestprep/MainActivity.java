package aleksandrkim.yandextestprep;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import aleksandrkim.yandextestprep.Db.Note;
import aleksandrkim.yandextestprep.NoteFeed.NoteFeedFragment;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        launchFragment();
    }

    private void launchFragment() {
        NoteFeedFragment feedFragment = new NoteFeedFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, feedFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        switch (item.getItemId()){
//            case R.id.menu_erase_all:
//                Realm realm = Realm.getDefaultInstance();
//                realm.executeTransactionAsync(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realm.where(Note.class).findAll().deleteAllFromRealm();
//                    }
//                });
//                return true;
//        }
//
////        if (id == R.id.homeAsUp) {
////            getSupportFragmentManager().popBackStack();
////            return true;
////        }
////        if (id == R.id.action_settings) {
////            Realm realm = Realm.getDefaultInstance();
////            realm.executeTransactionAsync(new Realm.Transaction() {
////                @Override
////                public void execute(Realm realm) {
////                    realm.where(Note.class).findAll().deleteAllFromRealm();
////                }
////            });
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
