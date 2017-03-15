package org.aguaclara.post.collect.tasks;

import org.aguaclara.post.collect.listeners.FormListDownloaderListener;
import org.aguaclara.post.collect.logic.FormDetails;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Etan on 3/10/2017.
 *
 * Used to download an updated form if it exists and determine which form is most up to date. This class
 * depends on a specific form naming convention in order to parse the title and correctly determine
 * which form is the most up to date in the database. I expose two methods. One checks online for a
 * newer form, and the other checks just the local list of forms. In either case, returns the newer form
 * name if it is more recent than the name passed in. If there is no newer form for either case, return
 * the name of the form passed in.
 */

public class UpdateBlankFormsTask{

    private DownloadFormListTask downloadFormListTask = new DownloadFormListTask();

    //Download the newer version of the forms passed. Don't want to clutter the list of forms, so we
    //only download the newer versions of forms that the user has already chosen to use.
    public void downloadNewerBlankFormsOnline(){

        //TODO get a list of the current blank forms from the database and produce a hash map of form<names,formDetails>
        //Filler:
        FormDetails form = new FormDetails("COLLECT-13-3-2017","download_url","manifest","id","version");

        //Get a hashmap of forms from the aggregate server
        DownloadFormListTask downloadFormListTask = new DownloadFormListTask();
        HashMap<String, FormDetails> availableForms = downloadFormListTask.doInBackground();
        //If the form is still available for download, don't download newer form
        if(availableForms.containsKey(form.formID)){}
        else{
            for(String availableForm : availableForms.keySet()){
                //We found a newer form
                if(availableForm == whichIsNewer(form, availableForms.get(availableForm))){
                    System.out.println("I found a newer form! TODO: we need to download this newer form and send it to the DB");
                }
            }
        }
    }

    public static void checkForNewerBlankFormLocal(String formName){
    }

    private void deleteOutdatedForms(){};

    //TODO Determine and return which form is the newest version?
    private String whichIsNewer(FormDetails formName1, FormDetails formName2){
        return formName1.formID;
    }


//    public void formListDownloadingComplete(HashMap<String, FormDetails> value){
//            //When this completes, you should check to see if this list has a newer form listed. If so, download that newer form.
//        System.out.print(value);
//    }
}
