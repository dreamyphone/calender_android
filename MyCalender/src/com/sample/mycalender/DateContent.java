package com.sample.mycalender;

import java.util.ArrayList;
import java.util.List;

public class DateContent {

	private List<StringBuffer> contentList;

	public DateContent() {
		// TODO 自動生成されたコンストラクター・スタブ
		this.contentList = new ArrayList<StringBuffer>();
	}

	public void addContent(String content) {
		this.contentList.add(new StringBuffer(content));
	}

	public void setContent(int location, String content) {
		this.contentList.set(location, new StringBuffer(content));

	}

	public String getContent(int location) {
		return this.contentList.get(location).toString();
	}

	public List<StringBuffer> getContentList() {
		return this.contentList;
	}

	public void clearList() {
		this.contentList.clear();
	}
}
