package org.aguaclara.post.collect.POST;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.aguaclara.post.collect.R;
import org.aguaclara.post.collect.application.Collect;
import org.aguaclara.post.collect.listeners.DiskSyncListener;
import org.aguaclara.post.collect.listeners.FormDownloaderListener;
import org.aguaclara.post.collect.listeners.FormListDownloaderListener;
import org.aguaclara.post.collect.logic.FormDetails;
import org.aguaclara.post.collect.provider.FormsProviderAPI;
import org.aguaclara.post.collect.tasks.DownloadFormListTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Etan on 3/10/2017.
 *
 * THIS IS A POST TASK
 *
 * Used to download an updated form if it exists and determine which form is most up to date. This class
 * depends on a specific form naming convention in order to parse the title and correctly determine
 * which form is the most up to date in the database. I expose two methods. One checks online for a
 * newer form, and the other checks just the local list of forms. In either case, returns the newer form
 * name if it is more recent than the name passed in. If there is no newer form for either case, return
 * the name of the form passed in.
 */

public class UpdateBlankFormsTask implements FormListDownloaderListener, FormDownloaderListener{

    private static final String t = "UpdateBlankFormsTask";

    private DownloadFormListTask mDownloadFormListTask;
    private HashMap<String, FormDetails> mFormNamesAndURLs = new HashMap<String,FormDetails>();
    private HashMap<String, FormDetails> mNewerFormNamesAndURLs = new HashMap<String,FormDetails>();

    /**
     * Download the newer version of the forms passed. Don't want to clutter the list of forms, so
     * we only download the newer versions of forms that the user has already chosen to use.
     *
     *
     */
    public void downloadNewerBlankForms(){

        //TODO get a list of the current blank forms from the database and produce a hash map of form<names,formDetails>
        //Filler:
        FormDetails form = new FormDetails("COLLECT-13-3-2017","download_url","manifest","id","version");

        downloadFormList();
        //If the form is still available for download, don't download newer form
        if(mFormNamesAndURLs.containsKey(form.formID)){}
        else{
            for(String availableForm : mFormNamesAndURLs.keySet()){
                //We found a newer form
                if(availableForm.equals(Newer(form, mFormNamesAndURLs.get(availableForm)))){
                    System.out.println("I found a newer form! TODO: we need to download this newer form and send it to the DB");
                    mNewerFormNamesAndURLs.put(availableForm, mFormNamesAndURLs.get(availableForm));
                }
            }
        }
    }

    private static void checkForNewerBlankFormLocal(String formName){
    }

    private void deleteOutdatedForms(){};

    //TODO Determine and return which form is the newest version? for now, we say the one on the DB
    // is always newer (form 1) so we always download all blank forms. This needs to be moved to the
    // POSTFormDetails class
    private String Newer(FormDetails formName1, FormDetails formName2){
        return formName1.formID;
    }

    private void downloadFormList() {
        mFormNamesAndURLs = new HashMap<String, FormDetails>();
        if (mDownloadFormListTask != null &&
                mDownloadFormListTask.getStatus() != AsyncTask.Status.FINISHED) {
            return; // we are already doing the download!!!
        } else if (mDownloadFormListTask != null) {
            mDownloadFormListTask.setDownloaderListener(null);
            mDownloadFormListTask.cancel(true);
            mDownloadFormListTask = null;
        }

        mDownloadFormListTask = new DownloadFormListTask();
        mDownloadFormListTask.setDownloaderListener(this);
        mDownloadFormListTask.execute();
        System.out.println("The available forms are: " + mFormNamesAndURLs);

    }
    private void determineNewerForms(){
               if (mFormNamesAndURLs != null){
                   for (FormDetails form : mFormNamesAndURLs.values()){

                   }
               }
    }

    //TODO I need to finish this method so that it can return the list of forms already downloaded. The Query isn't working right now.
    private ArrayList<String> getFormsFromDB(){
        String[] selectionArgs = {};
        String selection = "ALL";
        String[] fields = { FormsProviderAPI.FormsColumns.DISPLAY_NAME };
        ArrayList<String> formIds = new ArrayList<>();

        Cursor formCursor = null;
        try {
            formCursor = Collect.getInstance().getContentResolver().query(FormsProviderAPI.FormsColumns.CONTENT_URI, fields, selection, selectionArgs, null);
            if (formCursor.getCount() == 0) {
                return formIds;
            }
            //Try to populate the ArrayList with the form names
        } finally{
            return formIds;
        }

    }

    // ====================Interface methods=====================================//

    // Fills in the mFormNamesAndURLs with the available form details.
    public void formListDownloadingComplete(HashMap<String, FormDetails> result){
        mDownloadFormListTask.setDownloaderListener(null);
        mDownloadFormListTask = null;

        if (result == null) {
            Log.e(t,"Formlist Downloading returned null.  That shouldn't happen");
            // Just displayes "error occured" to the user, but this should never happen.
            return;
        }

        if (result.containsKey(DownloadFormListTask.DL_AUTH_REQUIRED)) {
            // need authorization
            Log.e(t,"Formlist Downloading needs authentication");
        } else if (result.containsKey(DownloadFormListTask.DL_ERROR_MSG)) {
            // Download failed
            Log.e(t,"Formlist Downloading failed");
        } else {
            // Everything worked. Clear the list and add the results.
            mFormNamesAndURLs = result;
            Log.i(t,"FormList downloaded and the available forms are: " + result);
        }

        getFormsFromDB();

    };
    public void formsDownloadingComplete(HashMap<FormDetails, String> result){
        System.out.println("The forms have been downloaded and are:" + result);
    };
    public void progressUpdate(String currentFile, int progress, int total){
        System.out.println("Progress update: on " + currentFile + "with progress of " + progress + "out of a total of " + total);
    };
}
