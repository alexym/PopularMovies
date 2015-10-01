package alexym.com.popularmovies;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements SortOrderDialog.NoticeDialogListener {
    DialogFragment newFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.containerMain, new MainActivityFragment())
//                    .commit();
            Bundle arguments = new Bundle();
            //arguments.putString("id", "id");
            MainActivityFragment fragment = MainActivityFragment.newInstance(arguments);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.containerMain,fragment, MainActivityFragment.TAG);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sortOrder) {
            sortOrderDiag();
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intentSettings = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intentSettings);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogSelectItemClick(String sortOrderValue) {
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(getString(R.string.pref_sort_order_key), sortOrderValue).commit();
        //String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString((R.string.pref_sort_order_most_popular)));
        //Se recarga el fragment
        newFragment.dismiss();
        Bundle arguments = new Bundle();
        MainActivityFragment fragment = MainActivityFragment.newInstance(arguments);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.containerMain,fragment, MainActivityFragment.TAG);
        ft.commit();
    }
    private void sortOrderDiag(){
        newFragment = new SortOrderDialog();
        newFragment.show(getFragmentManager(), "sortOrderDialog");
    }
}
