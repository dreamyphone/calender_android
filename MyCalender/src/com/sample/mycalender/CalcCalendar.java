package com.sample.mycalender;

import java.util.Calendar;

abstract class CalcCalendar {
	public static String[] dayOfWeekString = {
			"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"
	};
	public Calendar calendar = null;

	abstract void CalcCalendarDate(MonthlyDate[][] monthlyDate);
	abstract void CalcCalendarHeader(int[] headerIndex);
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

}
