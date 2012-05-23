package list.fragment;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.mainListDB.Comment;
import com.mainListDB.CommentsDataSource;
import com.otherDB.Constants;

public class MainListFragment extends Fragment {

	ListView listView;
	EditText editText;
	ArrayAdapter<Comment> arrAdpt;
	private CommentsDataSource datasource;
	SQLiteDatabase database;
	List<Comment> ListValues;
	Comment cmt;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		datasource = new CommentsDataSource(this.getActivity());
		datasource.open();

		ListValues = datasource.getAllComments();

		View view = inflater.inflate(R.layout.fragment_basic, container, false);

		editText = (EditText) view.findViewById(R.id.editText1);

		arrAdpt = new ArrayAdapter<Comment>(this.getActivity(), android.R.layout.simple_list_item_1, ListValues);

		Button btnAdd = (Button) view.findViewById(R.id.button1);
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String stringText = editText.getText().toString();

				if (stringText.equals("") || stringText.equals(null)) {
					Toast.makeText(getActivity(), "Cannot be empty", Toast.LENGTH_SHORT).show();
				} else {
					Comment comment = null;
					comment = datasource.createComment(stringText);
					arrAdpt.add(comment);
					editText.setText("");
					Toast.makeText(getActivity(), "New Entry Added", Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button btn2Delete = (Button) view.findViewById(R.id.button2);
		btn2Delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!ListValues.isEmpty()) {
					cmt = ListValues.get(ListValues.size() - 1);
					datasource.deleteComment(cmt);
					arrAdpt.remove(cmt);
					arrAdpt.notifyDataSetChanged();
					Toast.makeText(getActivity(), "Entry Deleted", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "Cannot delete Empty field", Toast.LENGTH_SHORT).show();
				}
			}
		});

		listView = (ListView) view.findViewById(R.id.listView1);
		listView.setAdapter(arrAdpt);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				cmt = (Comment) listView.getItemAtPosition(pos);

				Constants.id = (int) cmt.getId();

				FragmentManager fm = getFragmentManager();
				Fragment fragment = fm.findFragmentById(R.id.fragment_content);

				if (fragment == null) {
					FragmentTransaction ft = fm.beginTransaction();
					ft.add(R.id.fragment_content, new MainListFragment());
					ft.addToBackStack(null);
					ft.commit();
				} else {
					FragmentTransaction ft = fm.beginTransaction();
					ft.replace(R.id.fragment_content, new SubListFraagment());
					ft.addToBackStack(null);
					ft.commit();
				}
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	public void onPause() {
		datasource.close();
		super.onPause();
	}
}
