package uk.co.firstchoice.viking.gui;

public interface ISearchResult {

	static final String ROW_SEP = "~";

	static final String COL_SEP = "^";

	public String buildCSVString();

}