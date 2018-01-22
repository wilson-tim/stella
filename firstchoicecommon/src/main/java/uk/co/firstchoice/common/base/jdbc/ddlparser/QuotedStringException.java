package uk.co.firstchoice.common.base.jdbc.ddlparser;

public class QuotedStringException extends Exception {
    public QuotedStringException() {
        super();
    }

    public QuotedStringException(String s) {
        super(s);
    }
}
