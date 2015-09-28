package alexym.com.popularmovies;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by Cloudco on 23/09/15.
 */

public class SortOrderDialog extends DialogFragment {
    SharedPreferences prefs;
    String sortOrder;
    int actualSortOrderIndex = -1;
    String[] arrayString;

    public interface NoticeDialogListener {
        public void onDialogSelectItemClick(String sortOrderValue);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Carga el orden actual
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString((R.string.pref_sort_order_most_popular)));
        arrayString = getResources().getStringArray(R.array.pre_sort_order_values);
        for(int i = 0 ; i< arrayString.length;i++){
            if(arrayString[i].equals(sortOrder)){
                actualSortOrderIndex = i;
            }
        }
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SortOrderDialog.NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Log.i("sortorder diaglo", "es el " + sortOrder);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.pref_sort_order_label)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.pre_sort_order_options, actualSortOrderIndex, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogSelectItemClick(arrayString[which]);
                    }
                });
        return builder.create();
    }
}
