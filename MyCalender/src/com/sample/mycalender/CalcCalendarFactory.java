package com.sample.mycalender;

public class CalcCalendarFactory {
	static enum CalendarType {
		NORMAL,
		DAY_OF_WEEK,
	};

	static CalcCalendar CreateCalcCalendar(CalendarType type) {
		if (CalcCalendarFactory.CalendarType.NORMAL == type) {
			return new CalcCalendarNormal();
		} else if (CalcCalendarFactory.CalendarType.DAY_OF_WEEK == type) {
			return new CalcCalendarDayOfWeek();
		}
		return null;
	}

}
