package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;


public class GetGuideNum extends Activity implements SearchView.OnQueryTextListener {

    private ArrayList<String> mArrayList = new ArrayList<String>();
    private ListView mListView;
    private SearchView mSearchView;
    private Button mOkButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_guide_num);

        mListView  = (ListView) findViewById(R.id.listView);
        mSearchView = (SearchView) findViewById(R.id.searchView);

        ArrayList contacts = getContactList();


        for(int i = 0; i < mArrayList.size(); i++){
            //mListView.setAdapter(mArrayList.get(i));
            Log.d("ListView", mArrayList.get(i));
        }

        mListView.setTextFilterEnabled(true);
        setupSearchView();
    }

    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {

        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    private ArrayList getContactList() {
        ArrayList contactList = new ArrayList();

        // Get URI of phone contracts
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

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

                // Insert Name Values of Contacts into ListView
                mArrayList.add(acontact.getName());

                //Log.d("contact",acontact.getPhonenum());
                //Log.d("contact",acontact.getName());

                contactList.add(acontact);

            }while (contactCursor.moveToNext());

        }
        return contactList;
    }

}

