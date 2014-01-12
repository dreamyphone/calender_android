package com.sample.mycalender;

import java.util.Calendar;

public class CalcCalendarDayOfWeek extends CalcCalendar {

	/**
	 * コンストラクタ
	 * @param calendar
	 */
	public CalcCalendarDayOfWeek(Calendar calendar) {
		this.calendar = calendar;
	}

	public CalcCalendarDayOfWeek() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void CalcCalendarDate(MonthlyDate[][] monthlyDate) {
		// TODO 自動生成されたメソッド・スタブ
		//	配列の初期化
		for (int rowCount = 0; rowCount < 6; rowCount++) {
			for (int colCount = 0; colCount < 7; colCount++) {
				monthlyDate[rowCount][colCount] = null;
			}
		}


		int lastDayOfMonth = this.calendar.getActualMaximum(Calendar.DATE);
		int rowCount = 0, colCount = 0;
		for (int dayCount = 1; dayCount <= lastDayOfMonth; dayCount++) {
			monthlyDate[rowCount][colCount] = new MonthlyDate(
					this.calendar.get(Calendar.YEAR),
					this.calendar.get(Calendar.MONTH),
					dayCount);
			if (6 == colCount) {
				colCount = 0;
				rowCount++;
			} else {
				colCount++;
			}
		}
		/*
		for (int rowCount = 0; rowCount < monthlyDate.length; rowCount++) {
			for (int colCount = 0; colCount < monthlyDate[rowCount].length; colCount++) {
				if (monthlyDate[rowCount][colCount] != null) {
					(monthlyDate[rowCount][colCount]).setDate(colCount);
				}
			}
		}
		*/
	}

	/**
	 * カレンダーのヘッダーに表示する文字列のインデックスを計算して、
	 * セットする。
	 */
	@Override
	public void CalcCalendarHeader(int[] calendarHeader) {
		// TODO 自動生成されたメソッド・スタブ
		//	配列の初期化
		for (int arrayIndex = 0; arrayIndex < calendarHeader.length; arrayIndex++) {
			calendarHeader[arrayIndex] = 0;
		}

		//	カレンダーの日付を、一日に変更
		int currentDayofMonthBackup = this.calendar.get(Calendar.DATE);		//	現在の日付をバックアップ
		this.calendar.set(Calendar.DATE, 1);
		int dayOfWeekIndex = this.calendar.get(Calendar.DAY_OF_WEEK);
		dayOfWeekIndex--;		//	日曜日から0ベースで開始するために、-1

		//	配列にインデックスをセット
		for (int loopCout = 0; loopCout < calendarHeader.length; loopCout++) {
			calendarHeader[loopCout] = dayOfWeekIndex;
			if (dayOfWeekIndex == Calendar.SATURDAY - 1) {	//	Calendarクラスは、1ベースで曜日を定義
				dayOfWeekIndex = Calendar.SUNDAY - 1;
			} else {
				dayOfWeekIndex++;
			}
		}
		this.calendar.set(Calendar.DATE, currentDayofMonthBackup);

	}

}
