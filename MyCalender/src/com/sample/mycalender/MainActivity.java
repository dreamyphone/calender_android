package com.sample.mycalender;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//	テキストビューのAlignmentを調整
		TextView dateTextView = (TextView)findViewById(R.id.txtDateView);
		dateTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

		this.dateChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		getMenuInflater().inflate(R.menu.calendar_menu, menu);

		return true;
	}

	public void onNextButtonClick(View view) {
		CustomCalendarView customCalendar = (CustomCalendarView)findViewById(R.id.customCalendarView1);
		if (customCalendar.getMonth() == 12) {
			customCalendar.setYear(customCalendar.getYear() + 1);
			customCalendar.setMonth(1);
		} else {
			customCalendar.setMonth(customCalendar.getMonth() + 1);
		}
		customCalendar.clearContent();
		this.dateChanged();
	}

	public void onPrevButtonClick(View view) {
		CustomCalendarView customCalendar = (CustomCalendarView)findViewById(R.id.customCalendarView1);
		if (customCalendar.getMonth() == 1) {
			customCalendar.setYear(customCalendar.getYear() - 1);
			customCalendar.setMonth(12);
		} else {
			customCalendar.setMonth(customCalendar.getMonth() - 1);
		}
		customCalendar.clearContent();
		this.dateChanged();
	}

	/**
	 * 表示されている日付を変更する。
	 */
	public void dateChanged() {
		CustomCalendarView customCalendar = (CustomCalendarView)findViewById(R.id.customCalendarView1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM", Locale.JAPAN);
		Calendar calendar = new GregorianCalendar(customCalendar.getYear() - 1900,
				customCalendar.getMonth() - 1,
				customCalendar.getDay());
		String dateString = simpleDateFormat.format(calendar.getTime());
		TextView dateTextView = (TextView)findViewById(R.id.txtDateView);
		dateTextView.setText(dateString);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("Tag", "Call onOptionsItemSelected");

		CustomCalendarView customCalendar = (CustomCalendarView)findViewById(R.id.customCalendarView1);
		switch(item.getItemId()) {
		case R.id.set_normal_view:
			customCalendar.changeStyle(CalcCalendarFactory.CalendarType.NORMAL);
			break;

		case R.id.set_day_of_week_view:
			customCalendar.changeStyle(CalcCalendarFactory.CalendarType.DAY_OF_WEEK);
			break;

		case R.id.add_content:
			customCalendar.addContent(new DateContent());
			break;

		default:
			break;
		}
		return true;
	}
}
