package com.sample.mycalender;

import java.util.Calendar;

public class CalcCalendarNormal extends CalcCalendar {

	/**
	 * コンストラクタ
	 * @param calendar	演算ターゲットになるカレンダークラス
	 */
	public CalcCalendarNormal(Calendar calendar) {
		this.calendar = calendar;
	}

	public CalcCalendarNormal() {}

	@Override
	public void CalcCalendarDate(MonthlyDate[][] monthlyDate) {
		// TODO 自動生成されたメソッド・スタブ
		//	日付の配列を初期化
		int rowCount, colCount;
		for (rowCount = 0; rowCount < 6; rowCount++) {
			for (colCount = 0; colCount < 7; colCount++) {
				monthlyDate[rowCount][colCount] = null;
			}
		}

		//	週の情報の取得
		int dateBackup = this.calendar.get(Calendar.DATE);
		this.calendar.set(Calendar.DATE, 1);	//	日付を1日に設定
		int dayOfWeek = this.calendar.get(Calendar.DAY_OF_WEEK);

		//	月の最終日を取得
		int lastDayOfMonth = this.calendar.getActualMaximum(Calendar.DATE);

		//	日付情報の計算を開始
		rowCount = 0;
		colCount = dayOfWeek - 1;
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
		this.calendar.set(Calendar.DATE, dateBackup);
	}

	@Override
	public void CalcCalendarHeader(int[] headerIndex) {
		// TODO 自動生成されたメソッド・スタブ
		for (int loopCount = 0; loopCount < 7; loopCount++) {
			headerIndex[loopCount] = Calendar.SUNDAY  + loopCount - 1;
		}
	}
}
