package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;

import java.util.ArrayList;


public class GetGuideNum extends Activity implements SearchView.OnQueryTextListener{

    private ArrayList<Contact> mContact = new ArrayList<Contact>();
    private ListView mListView;
    private SearchView mSearchView;
    private Button mOkButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_guide_num);

        mListView  = (ListView) findViewById(R.id.listView);
        mSearchView = (SearchView) findViewById(R.id.searchView);

        getContactList();

        ContactAdapter cAdapter = new ContactAdapter(this, R.layout.row, mContact);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mListView.setAdapter(cAdapter);
        mListView.setTextFilterEnabled(true);
        setupSearchView();
    }

    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        //mSearchView.setQueryHint("검색");
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


    private class ContactAdapter extends ArrayAdapter<Contact> {

        private ArrayList<Contact> items;
        private ArrayList<Contact> origItems;

        public ContactAdapter (Context context, int textViewResourceId, ArrayList<Contact> _items) {
                super(context, textViewResourceId, _items);
                this.items = _items;
        }

        public Filter getFilter() {
            return new Filter() {

                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults ret = new FilterResults();
                    final ArrayList<Contact> results = new ArrayList<Contact>();

                    if(origItems == null) {
                        origItems = items;
                    }

                    if(constraint != null) {
                        if(origItems != null && origItems.size() > 0) {
                            for(final Contact c : origItems) {
                                if(c.getName().toLowerCase().contains(constraint)) {
                                    results.add(c);
                                }
                            }
                        }

                        Log.d("Constraint", constraint.toString());
                        Log.d("Filter", String.valueOf(results.size()));
                        ret.values = results;
                        ret.count = results.size();
                    }

                    return ret;
                }

                protected void publishResults(CharSequence constraint, FilterResults results) {
                        items = (ArrayList<Contact>) results.values;
                        Log.d("Items", String.valueOf(items.size()));
                        notifyDataSetChanged();
                }
            };
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if(v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }

            if(items.size() > position ) {
                Contact temp = items.get(position);

                if (temp != null) {
                    CheckBox cb1 = (CheckBox) v.findViewById(R.id.Name);
                    TextView tb1 = (TextView) v.findViewById(R.id.phoneNum);

                    if (cb1 != null) {
                        cb1.setText(temp.getName());
                    }
                    if (tb1 != null) {
                        tb1.setText("전화번호 : " + temp.getNumber());
                    }
                }
            }

            return v;
        }
    }

    private void getContactList() {

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

                Contact aContact = new Contact();

                if(phoneNumber.length() == 10) {
                    phoneNumber = phoneNumber.substring(0,3) + "-"
                                  + phoneNumber.substring(3,6) + "-"
                                  + phoneNumber.substring(6);
                } else if(phoneNumber.length() > 8) {
                    phoneNumber = phoneNumber.substring(0,3) + "-"
                                  + phoneNumber.substring(3,7) + "-"
                                  + phoneNumber.substring(7);
                }

                // Insert Name Values of Contacts into ListView
                aContact.setName(contactCursor.getString(0));
                aContact.setNumber(phoneNumber);
                mContact.add(aContact);

                //Log.d("contact",acontact.getPhonenum());
                //Log.d("contact",acontact.getName());

            }while (contactCursor.moveToNext());
        }
    }

    class Contact {
        private String Number;
        private CheckBox Check = new CheckBox(getApplicationContext());

        public Contact () {
        }

        public Contact (String _name, String _number) {
            this.Check.setText(_name);
            this.Number = _number;
        }

        public void setName(String name) {
            this.Check.setText(name);
        }

        public void setNumber(String num) {
            this.Number = num;
        }

        public String getName() {
            return (String) this.Check.getText();
        }

        public String getNumber() {
            return this.Number;
        }

        public boolean getChecked() {
            return this.Check.isChecked();
        }
    }

}

