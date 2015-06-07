package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Toast;

import java.util.ArrayList;

public class GetGuideNum extends Activity implements SearchView.OnQueryTextListener{

    private ArrayList<Contact> mContact = new ArrayList<Contact>();
    private ListView mListView;
    private SearchView mSearchView;

    String dbName = "vltList.db";
    String tableName = "vltListTable";
    int dbMode = Context.MODE_PRIVATE;

    static SQLiteDatabase db;

    private ListView vltList;
    private ListView valueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_guide_num);

        mListView  = (ListView) findViewById(R.id.listView);
        mSearchView = (SearchView) findViewById(R.id.searchView);

        Context mContext = getApplicationContext();
        getContactList();
        ContactAdapter cAdapter = new ContactAdapter(mContext, R.layout.row, mContact);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mListView.setAdapter(cAdapter);
        mListView.setTextFilterEnabled(true);
        setupSearchView();

        // Make DB Table
            db = openOrCreateDatabase(dbName, dbMode, null);
            String sql = "create table if not exists " + tableName + "(id integer primary key autoincrement, name text not null, phone text not null)";
            db.execSQL(sql);
            Log.d("DB", "DB Create");

        //DB Insert
        findViewById(R.id.Register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueList = (ListView) findViewById(R.id.listView);
                ContactAdapter tempAdapter = (ContactAdapter) valueList.getAdapter();

                String sql = "select * from " + tableName + ";";
                Cursor results = db.rawQuery(sql, null);

                results.moveToFirst();
                ArrayList<Contact> originalValues = new ArrayList<Contact>();

                while(!results.isAfterLast()){
                    int id = results.getInt(0);
                    String name = results.getString(1);
                    String number = results.getString(2);

                    Contact aContact = new Contact();

                    aContact.setName(name);
                    aContact.setNumber(number);

                    originalValues.add(aContact);

                    results.moveToNext();
                }
                results.close();

                ArrayList<Contact> temp = tempAdapter.getItem();

                for (int i = 0; i < temp.size(); i++) {
                    Log.d("DB", temp.get(i).getName());
                    Log.d("DB", temp.get(i).getNumber());
                    Log.d("DB", String.valueOf(temp.get(i).getChecked()));

                    if(temp.get(i).getChecked() == true ) {

                        boolean overlap = false;

                        for(int j = 0; j < originalValues.size(); j++) {
                            if(originalValues.get(j).getName().equals(temp.get(i).getName()))
                                overlap = true;
                        }

                        if(!overlap) {
                            sql = "insert into " + tableName + " values (NULL, '" + temp.get(i).getName() + "', '" + temp.get(i).getNumber() + "');";
                            db.execSQL(sql);
                        }
                    }
                }
            }
        });

    }

    // 모든 Data 읽기
    public void selectAll(){
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();

        while(!results.isAfterLast()){
            int id = results.getInt(0);
            String name = results.getString(1);
            String number = results.getString(2);

            results.moveToNext();
        }
        results.close();
    }

    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
        //mSearchView.setQueryHint("검색");
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        ContactAdapter ca = (ContactAdapter) mListView.getAdapter();

        if (TextUtils.isEmpty(query)) {
            ca.getFilter().filter(null);
        } else {
            ca.getFilter().filter(query);
        }
        return true;
    }

    public class ContactAdapter extends ArrayAdapter<Contact> {

        private ArrayList<Contact> items;
        private ArrayList<Contact> origItems;
        private boolean[] checked;

        public ArrayList<Contact> getItem() {
            return items;
        }

        public ContactAdapter (Context context, int textViewResourceId, ArrayList<Contact> _items) {
                super(context, textViewResourceId, _items);
                this.items = _items;
                checked = new boolean[items.size()];

                for(int i = 0; i < checked.length; i++) {
                    checked[i] = false;
                }
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
                        checked = new boolean[items.size()];

                        for(int i = 0; i < checked.length; i++) {
                            checked[i] = false;
                        }

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
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);;

            v = vi.inflate(R.layout.row, null);

            final int cBoxPosition = position;

            if(items.size() > position) {
                Log.d("getView ",String.valueOf(position));
                Log.d("getView Value ", items.get(position).getName());
                Contact temp = items.get(position);

                if (temp != null) {
                    CheckBox cb1 = (CheckBox) v.findViewById(R.id.Name);
                    TextView tb1 = (TextView) v.findViewById(R.id.phoneNum);

                    Log.d("Check", String.valueOf(temp.getChecked()));

                    if (cb1 != null) {
                        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked) {
                                    checked[cBoxPosition] = true;
                                    items.get(cBoxPosition).setChecked(true);
                                } else {
                                    checked[cBoxPosition] = false;
                                    items.get(cBoxPosition).setChecked(false);
                                }
                            }
                        });

                        cb1.setText(temp.getName());
                        if(checked[cBoxPosition] == true)
                            cb1.setChecked(true);
                        else
                            cb1.setChecked(false);
                    }

                    if (tb1 != null) {
                        tb1.setText("전화번호 : " + temp.getNumber());
                    }
                }
            } else if(items.size() != 0 && items.size() <= position) {
                v = vi.inflate(R.layout.empty, null);
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

        public void setChecked(boolean check) { this.Check.setChecked(check); }
    }
}