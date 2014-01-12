/**
 *
 */
package com.sample.mycalender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * @author Kensukse
 *
 */
public class CustomCalendarView extends View {
	
	private Context context = null;
	
	//	セルのサイズのパラメータ
	protected float cellWidth;
	protected float cellHeight;
	protected float captionWidth;
	protected float captionHeight;
	protected float headerHeight;

	//	選択されたセルのインデックス
	protected int selectedCol;		//	行のインデックス
	protected int selectedRow;		//	列のインデックス

	//	サイズ
	protected float DEF_WIDTH = 320;
	protected float canvasWidth;
	protected float canvasHeight;
	protected float colLineLen;
	protected float rowLineLen;

	//	カレンダー情報
	protected Calendar calendar = Calendar.getInstance();
	protected int todayRow;
	protected int todayCol;

	//	色情報
	protected int selCellBackColorInt = Color.parseColor("#FF3333ff");
	protected int selCellFontColorInt = Color.parseColor("#FFFFFFFF");
	protected int regContentColorInt = Color.parseColor("#55CCFF00");
	protected int selSepLineColorInt = Color.parseColor("#FF000000");
	protected int selectedCellColorInt = Color.parseColor("#55FFCC66");
	protected int sundayTextColorInt = Color.parseColor("#FFFF0000");
	protected int thursdayTextColorInt = Color.parseColor("#FF0000FF");


	//	色情報 - Paintオブジェクト
	protected Paint selCellBackColor = new Paint();
	protected Paint selSepLineColor = new Paint();
	protected Paint selectedCellColor = new Paint();
	protected Paint regContentColor = new Paint();
	protected Paint weekdayText = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
	protected Paint sundayText = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
	protected Paint saturdayText = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

	//	セルに対応する矩形
	private int weekdayCount = 7;
	private int weekNumCount = 6;
	protected Rect todayRect = new Rect();

	//	曜日表示
	protected static final String[] weekdays = {
		"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"
	};
	protected static final int[] dayOfWeek = {
		Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
		Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY,
		Calendar.SATURDAY
	};
	//protected DateInfoParcelable[][] monthDayInfo = new DateInfoParcelable[this.weekNumCount][this.weekdayCount];
	
	//TODO もし、今月の一日目は金曜日の前であれば、このweekNumCount 変数の値は５になります。
	
	protected MonthlyDate[][] monthlyDate = new MonthlyDate[this.weekNumCount][this.weekdayCount];
	protected int[] headerIndex = new int[weekdayCount];

	//	カレンダーの表示形式設定
	private CalcCalendarFactory.CalendarType calendarType = CalcCalendarFactory.CalendarType.NORMAL;

	//	コンテンツの有無を示す矩形
	private List<Rect> rectList = new ArrayList<Rect>();

	/**
	 * 各種パラメータの初期化とカレンダーの算出を行う。
	 */
	private void init(Context context) {
		
		this.context = context;
		selSepLineColor.setStrokeWidth(2);

		//	色の設定
		this.selCellBackColor.setColor(this.selCellBackColorInt);
		this.selSepLineColor.setColor(this.selSepLineColorInt);
		this.selectedCellColor.setColor(this.selectedCellColorInt);
		this.regContentColor.setColor(this.regContentColorInt);
		this.sundayText.setColor(this.sundayTextColorInt);
		this.saturdayText.setColor(this.thursdayTextColorInt);

		//	文字列の設定
		this.weekdayText.setTextAlign(Paint.Align.CENTER);
		this.weekdayText.setTextSize(20);
		this.sundayText.setTextAlign(Paint.Align.CENTER);
		this.sundayText.setTextSize(20);
		this.saturdayText.setTextAlign(Paint.Align.CENTER);
		this.saturdayText.setTextSize(20);

		//	カレンダーの日付情報の更新
		this.calcCalendarDate();
		this.setToday(this.calendar.get(Calendar.DATE));

	}

	/**
	 * キャンバスの適切なサイズを計算する。
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.setMeasuredDimension(
				this.measureWidth(widthMeasureSpec),
				this.measureHeight(widthMeasureSpec));
	}

	/**
	 * キャンバスの適切な横幅を取得する
	 * @param widthMeasureSpec
	 * @return 計算した横幅
	 */
	public int measureWidth(int widthMeasureSpec) {
		int fixedWidth = 0;
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (MeasureSpec.EXACTLY == specMode) {
			fixedWidth = specSize;
		} else {
			fixedWidth = (int)this.canvasWidth +
					getPaddingLeft() + getPaddingRight();
			if (MeasureSpec.AT_MOST == specMode) {
				fixedWidth = Math.max(fixedWidth, specSize);
			}
		}
		return fixedWidth;
	}

	/**
	 * キャンバスの適切な縦幅を取得する
	 * @param heightMeasureSpec
	 * @return 計算したキャンバスの横幅
	 */
	public int measureHeight(int heightMeasureSpec) {
		int fixedHeight = 0;
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSize = MeasureSpec.getSize(heightMeasureSpec);

		if (MeasureSpec.EXACTLY == specMode) {
			fixedHeight = specSize;
		} else {
			fixedHeight = (int)this.canvasHeight +
					getPaddingTop() + getPaddingBottom();
			if (MeasureSpec.AT_MOST == specMode) {
				fixedHeight = Math.max(fixedHeight, specSize);
			}
		}
		return fixedHeight;
	}


	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		this.setSize(w, h);

	}

	private void setSize(int w, int h) {
		this.canvasHeight = (float)h;
		this.canvasWidth = (float)w;

		this.cellHeight = this.canvasHeight / (float)6.4;
		this.cellWidth = this.canvasWidth / (float)7.0;

		this.headerHeight = this.cellHeight * (float)0.4;

		this.colLineLen = this.cellHeight * (float)6.0 + this.headerHeight;
		this.rowLineLen = this.canvasWidth;
	}

	/* (非 Javadoc)
	 * @see android.view.ViewTreeObserver.OnDrawListener#onDraw()
	 */
	@Override
	public void onDraw(Canvas canvas) {
		// TODO 自動生成されたメソッド・スタブ
		super.onDraw(canvas);

		//	キャンバスのサイズを設定
		this.setSize(canvas.getWidth(), canvas.getWidth());

		this.drawSepLine(canvas);
		this.drawHeader(canvas);
		this.drawDate(canvas);
		this.select();
		this.drawRect(canvas);

		//this.invalidate();
	}

	/**
	 * Canvasに、カレンダーの区切り線を引く。
	 * @param canvas	キャンバス
	 */
	private void drawSepLine(Canvas canvas) {
		//	セルの区切り線を記入
		float lineStartPosX = (float)0.0;
		float lineStartPosY = (float)0.0;
		for (int colCount = 0; colCount < weekdayCount + 1; colCount++) {
			canvas.drawLine(lineStartPosX, lineStartPosY,
					lineStartPosX, lineStartPosY + this.canvasHeight,
					this.selSepLineColor);
			lineStartPosX += cellWidth;
		}

		lineStartPosX = (float)0.0;
		lineStartPosY = (float)0.0;
		for (int rowCount = 0; rowCount < weekNumCount + 2; rowCount++) {
			canvas.drawLine(0, lineStartPosY,
					this.canvasWidth, lineStartPosY,
					this.selSepLineColor);
			switch (rowCount) {
			case 0:
				lineStartPosY += headerHeight;
				break;

			default:
				lineStartPosY += cellHeight;
				break;
			}
		}
	}

	/**
	 * カレンダーのヘッダを作成する。
	 * @param canvas	キャンバス
	 */
	private void drawHeader(Canvas canvas) {
		//	曜日のヘッダーを記入
		int dayOfWeekIndex = 0;
		float xPos = this.cellWidth / 2;
		for (dayOfWeekIndex = 0; dayOfWeekIndex < 7; dayOfWeekIndex++) {
			Paint textPaint;
			int dayOfWeekStringIndex = headerIndex[dayOfWeekIndex];
			if ((CalcCalendar.dayOfWeekString[dayOfWeekStringIndex]).compareTo("Sun") == 0) {
				textPaint = sundayText;
			} else if ((CalcCalendar.dayOfWeekString[dayOfWeekStringIndex]).compareTo("Sat") == 0) {
				textPaint = saturdayText;
			} else {
				textPaint = weekdayText;
			}
			canvas.drawText(weekdays[this.headerIndex[dayOfWeekIndex]],
							 xPos,
							 (float)(this.headerHeight * 0.75),
							 textPaint);
			xPos += this.cellWidth;
		}
	}


	/**
	 * カレンダーの日付を記入する。
	 * @param canvas	キャンバス
	 */
	private void drawDate(Canvas canvas) {
		int rowCount = 0;
		int colCount = 0;

		float xPos = 0;
		float yPos = this.headerHeight * (float)0.75;
		for (rowCount = 0; rowCount < this.weekNumCount; rowCount++) {
			xPos = this.cellWidth / 2;
			yPos += this.cellHeight;
			for (colCount = 0; colCount < this.weekdayCount; colCount++) {
				Paint textPaint;
				if (this.monthlyDate[rowCount][colCount] != null) {
					int dayOfWeekStringIndex = headerIndex[colCount];
					if ((CalcCalendar.dayOfWeekString[dayOfWeekStringIndex]).compareTo("Sun") == 0) {
						textPaint = sundayText;
					} else if ((CalcCalendar.dayOfWeekString[dayOfWeekStringIndex]).compareTo("Sat") == 0) {
						textPaint = saturdayText;
					} else {
						textPaint = weekdayText;
					}
					canvas.drawText(String.format("%d", this.monthlyDate[rowCount][colCount].getDate()),
							xPos, yPos,
							textPaint);
				} else {
					textPaint = weekdayText;
					canvas.drawText(String.format(""), xPos, yPos, textPaint);
				}
				xPos += this.cellWidth;
			}
		}
	}

	/**
	 * カレンダーの内容を描画する。
	 * @param canvas
	 */
	private void drawRect(Canvas canvas) {
		if (null != this.rectList) {
			for (Rect rect : this.rectList) {
				canvas.drawRect(rect, this.regContentColor);
			}
		}

		//	選択された日を強調表示する。
		canvas.drawRect(this.todayRect, this.selectedCellColor);
	}



	/**
	 * カレンダーの日付を計算する。
	 */
	protected void calcCalendarDate() {
		//	表示情報の取得
		CalcCalendar calcWay = CalcCalendarFactory.CreateCalcCalendar(this.calendarType);
		calcWay.setCalendar(this.calendar);
		calcWay.CalcCalendarDate(this.monthlyDate);
		calcWay.CalcCalendarHeader(this.headerIndex);

		//	当日の座標を計算
		int today = this.calendar.get(Calendar.DATE);
		for (int rowCount = 0; rowCount < weekNumCount; rowCount++) {
			for (int colCount = 0; colCount < weekdayCount; colCount++) {
				if ((this.monthlyDate[rowCount][colCount] != null) &&
					(this.monthlyDate[rowCount][colCount].getDate() == today)) {
					this.selectedCol = colCount;
					this.selectedRow = rowCount;
					break;
				}
			}
		}
	}

	/**
	 * 表示形式を変更する。
	 * 表示形式に変更がなかった場合には、何もしない。
	 * @param calendarType	変更後の表示スタイル
	 */
	public void changeStyle(CalcCalendarFactory.CalendarType calendarType) {
		if (this.calendarType == calendarType)
			return;

		this.calendarType = calendarType;
		this.calcCalendarDate();
		invalidate();
	}


	/* コンストラクタ
	 *
	 */
	public CustomCalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init(context);
	}

	public CustomCalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	public CustomCalendarView(Context context) {
		super(context);
		this.init(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("event", String.format("x=%f y = %f", event.getX(), event.getY()));

		if (MotionEvent.ACTION_DOWN != event.getAction())
			return super.onTouchEvent(event);

		this.select(event.getX(), event.getY());
		this.invalidate();

		return false;
	}

	/**
	 * Canvasの座標から、選択されたセルを取得する
	 * @param xPos	Canvusのx座標
	 * @param yPos	Canvusのy座標
	 */
	private void select(float xPos, float yPos) {
		//	x座標から曜日を取得
		int colCount, rowCount;
		for (colCount = 0; colCount < weekdayCount; colCount++) {
			if ((xPos >= colCount * cellWidth) &&
				(xPos < (colCount + 1) * cellWidth)) {
				break;
			}
		}
		//	y座標から週を取得
		for (rowCount = 0; rowCount < weekNumCount; rowCount++) {
			if ((yPos >= headerHeight + (cellHeight * rowCount)) &&
				(yPos < headerHeight + (cellHeight * (rowCount + 1)))) {
				break;
			}
		}

		if ((colCount == weekdayCount) ||
			(rowCount == weekNumCount))
			return;

		if (this.monthlyDate[rowCount][colCount] != null) {
			this.selectedRow = rowCount;
			this.selectedCol = colCount;
			//this.select();
		}
		
		Toast.makeText(context, colCount + " " + rowCount, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 選択されている日にちのインデックスから、強調表示する矩形の
	 * 座標を設定する
	 */
	private void select() {
		this.todayRect.set(
				(int)(this.cellWidth * this.selectedCol),
				(int)(this.headerHeight + this.cellHeight * this.selectedRow),
				(int)(this.cellWidth * (this.selectedCol + 1)),
				(int)(this.headerHeight + this.cellHeight * (this.selectedRow + 1)));
	}

	/**
	 * 選択された日にちに対して、DateContentクラスを登録する。
	 * @param content	登録するコンテンツ
	 */
	public void addContent(DateContent content) {
		if (false == (this.monthlyDate[selectedRow][selectedCol]).isDateContent()) {	// 矩形の重複防止
			//	選択されたセルに対応する位置に、強調表示用の矩形を登録する。
			this.rectList.add(new Rect(
					(int)(this.cellWidth * this.selectedCol),
					(int)(this.headerHeight + this.cellHeight * this.selectedRow),
					(int)(this.cellWidth * (this.selectedCol + 1)),
					(int)(this.headerHeight + this.cellHeight * (this.selectedRow + 1))));
		}

		//	選択されたセルに、コンテンツを登録する。
		(this.monthlyDate[selectedRow][selectedCol]).addDateContent(content);

	}

	/**
	 * 全コンテンツ情報を削除する。
	 */
	public void clearContent() {
		this.rectList.clear();		// 矩形情報の削除

		// 日にちに登録されたコンテンツを削除
		for (MonthlyDate[] array : monthlyDate) {
			for (MonthlyDate element : array) {
				if ((null != element ) &&
					(true == element.isDateContent())) {
					element.clearContent();
				}
			}
		}

		this.invalidate();
	}

	/**
	 * 日付から、本日のセルのインデックスを取得する。
	 * @param today	本日の日付
	 */
	private void setToday(int today) {
		for (int rowCount = 0; rowCount < this.weekNumCount; rowCount++) {
			for (int colCount = 0; colCount < this.weekdayCount; colCount++) {
				if ((this.monthlyDate[rowCount][colCount] != null) &&
					(this.monthlyDate[rowCount][colCount]).getDate() == today) {
					this.selectedRow = rowCount;
					this.selectedCol = colCount;

					this.select();
					return;
				}
			}
		}
	}

	/**
	 * 表示されている西暦年を取得する。
	 * @return
	 */
	int getYear() {
		return monthlyDate[selectedRow][selectedCol].getYear();
	}

	/**
	 * 表示されている月を取得する
	 * @return
	 */
	int getMonth() {
		return (monthlyDate[selectedRow][selectedCol].getMonth() + 1);
	}

	/**
	 * 選択されている日付を取得する
	 * @return
	 */
	int getDay() {
		return monthlyDate[selectedRow][selectedCol].getDate();
	}

	/**
	 * 選択されている西暦年を変更する。
	 * @param year
	 */
	void setYear(int year) {
		if (monthlyDate[selectedRow][selectedCol].getYear() != year) {
			this.calendar.set(Calendar.YEAR, year);
			calcCalendarDate();
		}
	}

	/**
	 * 選択されている月を変更する。
	 * @param year	1月ベースの月
	 */
	void setMonth(int month) {
		if ((month < 1) || (month > 12))
			return;

		month -= 1;
		if (monthlyDate[selectedRow][selectedCol].getMonth() != month) {
			this.calendar.set(Calendar.MONTH, month);
			calcCalendarDate();
		}
	}
}
