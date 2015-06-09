package com.swstudio.dunkin.eye_fi;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 상원 on 2015-06-07.
 */

class DBId {
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ArrayList<String> names = new ArrayList<String>();
}

public class dbList extends Activity{

    String dbName = "vltList.db";
    String tableName = "vltListTable";
    int dbMode = Context.MODE_PRIVATE;
    private ArrayList<Contact> tmpContact = new ArrayList<Contact>();
    private DBId boxes = new DBId();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vltlist);

        final ListView vltList = (ListView) findViewById(R.id.volunteerList);

        String sql = "select * from " + tableName + ";";
        GetGuideNum.db = openOrCreateDatabase(dbName, dbMode, null);
        Cursor results = GetGuideNum.db.rawQuery(sql, null);

        results.moveToFirst();

        while(!results.isAfterLast()){
            Contact aContact = new Contact();

            int id = results.getInt(0);
            String name = results.getString(1);
            String number = results.getString(2);

            aContact.setName(name);
            aContact.setNumber(number);
            aContact.setChecked(false);

            Log.d("ID", String.valueOf(id));
            Log.d("aContact",aContact.getName());
            Log.d("aContact",aContact.getNumber());

            boxes.ids.add(id);
            boxes.names.add(name);

            tmpContact.add(aContact);

            results.moveToNext();
        }
        results.close();

        Log.d("DB Contact Size", String.valueOf(tmpContact.size()));

        for(int i = 0; i < tmpContact.size(); i++) {
            Log.d("DB Value", tmpContact.get(i).getName());
        }

        ContactAdapter tmpAdapter = new ContactAdapter(this, R.layout.row, tmpContact);
        vltList.setAdapter(tmpAdapter);
        vltList.setTextFilterEnabled(true);

        findViewById(R.id.unregister).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {

                        ContactAdapter temp = (ContactAdapter) vltList.getAdapter();
                        boolean[] checked = new boolean[temp.getCount()];

                        for(int i = 0; i < checked.length; i++)
                            checked[i] = false;

                        for(int i = 0; i < temp.getCount(); i++) {
                            if(temp.getItem(i).getChecked()) {
                                checked[i] = true;
                            }
                        }

                        for(int i = 0; i < checked.length; i++) {
                            if(checked[i] == true) {
                                int id = 0;
                                for(int j = 0; j < boxes.names.size(); j++) {
                                    if(temp.getItem(i).getName().equals(boxes.names.get(j))) {
                                        id = boxes.ids.get(j);
                                    }
                                }
                                String sql = "delete from " + tableName + " where id = "+ id +";";
                                GetGuideNum.db.execSQL(sql);
                            }
                        }
                    }
                }
        );

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
