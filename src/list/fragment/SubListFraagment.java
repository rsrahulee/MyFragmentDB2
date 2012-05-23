package list.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.R;
import com.otherDB.SubListModel;
import com.otherDB.SubListDataBase;

public class SubListFraagment extends Fragment {

	ListView listView;
	EditText editName;
	ArrayAdapter<String> arrAdpt;
	ArrayList<SubListModel> nameList = new ArrayList<SubListModel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		SubListDataBase myDB2 = new SubListDataBase(getActivity());
		myDB2.openDB();
		nameList = SubListDataBase.getAllComments();
		myDB2.close();

		String[] listNames = new String[nameList.size()];
		for (int i = 0; i < nameList.size(); i++) {
			System.out.println("");
			SubListModel data2Name = nameList.get(i);
			String name = data2Name.getName();
			listNames[i] = name;
		}

		View view = inflater.inflate(R.layout.fragment_basic, container, false);

		editName = (EditText) view.findViewById(R.id.editText1);

		arrAdpt = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, listNames);

		Button btnAdd = (Button) view.findViewById(R.id.button1);
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				String stringText = editName.getText().toString();

				if (stringText.equals("") || stringText.equals(null)) {
					Toast.makeText(getActivity(), "Cannot be empty", Toast.LENGTH_SHORT).show();
				} else {
					SubListDataBase myDB2 = new SubListDataBase(getActivity());
					SubListModel data = new SubListModel();
					data.setName(stringText);
					myDB2.openDB();
					SubListDataBase.insertDB(data);
					myDB2.close();

					editName.setText("");

					Toast.makeText(getActivity(), "New Entry Added", Toast.LENGTH_SHORT).show();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					ft.replace(R.id.fragment_content, new SubListFraagment());
					ft.addToBackStack(null);
					ft.commit();
				}
			}
		});

		Button btnDelete = (Button) view.findViewById(R.id.button2);
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				int listSize = nameList.size();

				if (listSize == 0) {
					Toast.makeText(getActivity(), "Cannot delete Empty field", Toast.LENGTH_SHORT).show();
				} else {
					SubListModel data2 = nameList.get(nameList.size() - 1);

					SubListDataBase myDB2 = new SubListDataBase(getActivity());
					myDB2.openDB();
					myDB2.deleteDB(data2);
					myDB2.close();

					FragmentManager fm = getFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					ft.replace(R.id.fragment_content, new SubListFraagment());
					ft.addToBackStack(null);
					ft.commit();
				}
			}
		});

		listView = (ListView) view.findViewById(R.id.listView1);
		listView.setAdapter(arrAdpt);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				Object o = listView.getItemAtPosition(pos);
				Log.v("rahul", o.toString());
				Toast.makeText(getActivity(), "Hi! You Clicked " + o, Toast.LENGTH_LONG).show();
			}
		});
		return view;
	}
}
