package list.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.android.R;

public class MyFragmentActivity extends FragmentActivity {

	private static Context cntx;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		cntx = this;

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragment_content);

		if (fragment == null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(R.id.fragment_content, new MainListFragment());
			ft.commit();
		}
	}

	public static Context getCntx() {
		// TODO Auto-generated method stub
		return cntx;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int iFragCnt = this.getSupportFragmentManager().getBackStackEntryCount();

			if (iFragCnt > 0) {

				FragmentManager fm = getSupportFragmentManager();
				Fragment fragment = fm.findFragmentById(R.id.fragment_content);

				String fragmentString = fragment.toString();

				String fragmentName = fragmentString.substring(0, 16);
				while (!fragmentName.equalsIgnoreCase("MainListFragment")) {

					iFragCnt = this.getSupportFragmentManager().getBackStackEntryCount();
					this.getSupportFragmentManager().popBackStackImmediate();// imp for pop
					iFragCnt = this.getSupportFragmentManager().getBackStackEntryCount();

					fragment = fm.findFragmentById(R.id.fragment_content);

					fragmentString = fragment.toString();

					fragmentName = fragmentString.substring(0, 16);

					if (fragmentName.equalsIgnoreCase("MainListFragment")) {
						break;
					}
				}
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Are you sure you want to exit?").setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MyFragmentActivity.this.finish();
							}
						}).setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				builder.show();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}