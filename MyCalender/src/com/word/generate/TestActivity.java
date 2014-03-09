package com.word.generate;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.sample.mycalender.R;
import com.word.database.WordContents;

public class TestActivity extends Activity implements DialogInterface.OnClickListener{
	
	private final String youdao = "/data/data/com.youdao.dict/databases/notes.db";
	private final String hj_dict = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HJApp/HJDict/data/db/DictDatabase";
	private final String hj_word = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HJApp/hjwordgames/HJUserDB.db";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO
		setContentView(R.layout.activity_test);
		//getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.dataDir;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}
	
	public void onYoudaoClick(View view){
		checkRoot();
		combineDB(youdao);
		Toast.makeText(this, R.string.bt_youdao, Toast.LENGTH_SHORT).show();
	}
	
	public void onDictClick(View view){
		checkRoot();
		combineDB(hj_dict);
		Toast.makeText(this, R.string.bt_hjdict, Toast.LENGTH_SHORT).show();
	}
	
	public void onWordClick(View view){
		checkRoot();
		combineDB(hj_word);
		Toast.makeText(this, R.string.bt_hjword, Toast.LENGTH_SHORT).show();
	}
	
	private void checkRoot(){
		if ( !new Root().isDeviceRooted()){
			new AlertDialog.Builder(this).setTitle("This is a test.")
			.setMessage("You must root your device first.")
			.setPositiveButton("OK", this).show();
		}else{
			String binPath = new ContextWrapper(this).getFilesDir().getAbsolutePath() + File.separator + "bin";
			try {
				InputStream input = getAssets().open("sqlite3");
				
				File file = new File(binPath);
				if ( !file.exists() ){
					new File(binPath).mkdir();
					OutputStream output = new FileOutputStream(new File(binPath + File.separator + "sqlite3"));
					byte[] cache = new byte[1024];
					int count ;
					while ( (count = input.read(cache)) != -1 ){
						output.write(cache, 0, count);
					}
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			WordContents contents = new WordContents(this);
			contents.getReadableDatabase();// generate database
			
			Runtime runtime = Runtime.getRuntime();
			try {
				Process process = runtime.exec("su");
				DataOutputStream stream = new DataOutputStream(process.getOutputStream());
				Log.d("test", "chmod 755 " + binPath + File.separator + "sqlite3");
				stream.writeBytes("chmod 755 " + binPath + File.separator + "sqlite3\n");
				stream.writeBytes("exist\n");
				stream.flush();
				stream.close();
					
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void combineDB(String DB_path){
		try {
			Process process = Runtime.getRuntime().exec("su");
			// -c /data/data/com.sample.mycalender/files/bin/sqlite3 /data/data/com.sample.mycalender/databases/words
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("cd /data/data/com.sample.mycalender/databases/ \n");
			os.writeBytes("../files/bin/sqlite3 words \n");
			os.writeBytes("attach \"" + DB_path + "\" as aDB; \n");
			os.writeBytes("INSERT INTO notes(word, phonetic, detail)"
					+ " select word, phonetic, detail from aDB.notes; \n");
			os.writeBytes(".q \n");
			
			os.flush();
			os.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
	}

}
