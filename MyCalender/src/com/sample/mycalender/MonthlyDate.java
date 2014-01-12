package com.sample.mycalender;

import java.util.Calendar;

public class MonthlyDate {
	private int year;
	private int month;
	private int dayOfMonth;
	private int dayOfWeekIndex;
	private DateContent dateContent = null;

	private Calendar calendar = Calendar.getInstance();

	/**
	 * コンストラクタ
	 */
	public MonthlyDate(int year, int month) {
		this.init(year, month, 1);
	}

	public MonthlyDate(int year, int month, int dayOfMonth) {
		this.init(year, month, dayOfMonth);
	}

	private void init(int year, int month, int dayOfMonth) {
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;

		this.calendar.set(Calendar.YEAR, year);
		this.calendar.set(Calendar.MONTH, month);
		this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	}

	/**
	 * 現在の西暦年を返す
	 * @return 現在の西暦年
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * 現在の月を返す
	 * @return 現在の年
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * 月の日付を返す
	 * @return	月の日にち
	 */
	public int getDate() {
		return this.dayOfMonth;
	}

	/**
	 * 週の曜日を返す。
	 * 日曜日を1とした各曜日
	 * @return
	 */
	public int getDayOfWeek() {
		return this.dayOfWeekIndex;
	}

	/**
	 * 日にちを設定する。
	 * @param dayOfMonth
	 */
	public void setDate(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	/**
	 * コンテンツリストを追加する。
	 * @param dateContent
	 */
	public void addDateContent(DateContent dateContent) {
		if (this.dateContent == null)
			this.dateContent = new DateContent();

		for (StringBuffer list : dateContent.getContentList()) {
			this.dateContent.addContent(list.toString());	//	コンテンツの内容をコピー
		}
	}

	/**
	 * コンテンツを取得する。
	 * @return
	 */
	public DateContent getDateContent() {
		return this.dateContent;
	}

	/**
	 * コンテンツをセットする。
	 * @param dateContent
	 */
	public void setDateContent(DateContent dateContent) {
		this.dateContent = dateContent;
	}

	/**
	 * コンテンツの登録状態を取得する
	 * @return
	 */
	public boolean isDateContent() {
		return this.dateContent == null ? false : true;
	}

	public void clearContent() {
		this.dateContent.clearList();
		this.dateContent = null;
	}
}
