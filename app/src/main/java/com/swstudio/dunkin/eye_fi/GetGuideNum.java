package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class GetGuideNum extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_guide_num);

        ArrayList contacts = getContactList();
    }

    private ArrayList getContactList() {
        ArrayList contactList = new ArrayList();

        // Get URI of phone contracts
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "COLLATE LOCALIZED ASC";

        Cursor contactCursor = getContentResolver().query(uri, projection, null, null, sortOrder);

        if(contactCursor.moveToFirst()) {
            do {
                String phoneNumber = contactCursor.getString(1).replaceAll("-", "");

                if(phoneNumber.length() == 10) {
                    phoneNumber = phoneNumber.substring(0,3) + "-"
                                  + phoneNumber.substring(3,6) + "-"
                                  + phoneNumber.substring(6);
                } else if(phoneNumber.length() > 8) {
                    phoneNumber = phoneNumber.substring(0,3) + "-"
                                  + phoneNumber.substring(3,7) + "-"
                                  + phoneNumber.substring(7);
                }

                Contact acontact = new Contact();

                acontact.setPhonenum(phoneNumber);
                acontact.setName(contactCursor.getString(0));

                Log.d("contact",acontact.getPhonenum());
                Log.d("contact",acontact.getName());

                contactList.add(acontact);

            }while (contactCursor.moveToNext());

        }
        return contactList;
    }
}

