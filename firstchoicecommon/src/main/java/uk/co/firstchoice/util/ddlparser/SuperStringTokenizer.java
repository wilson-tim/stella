package uk.co.firstchoice.util.ddlparser;

import java.util.StringTokenizer;
import java.util.Vector;

/** class used in ddl processor to tokenise a string so can be parsed through ddl */
public class SuperStringTokenizer extends StringTokenizer {
	String delimList = " ";

	public SuperStringTokenizer(String s) {
		super(s);
	}

	public SuperStringTokenizer(String s, String delim) {
		super(s, delim);
		delimList = delim;
	}

	public SuperStringTokenizer(String s, String delim, boolean ret) {
		super(s, delim, ret);
		delimList = delim;
	}

	public Vector getVectorList() {
		Vector v = new Vector(10, 10);

		while (hasMoreElements()) {
			v.addElement(nextToken());
		}

		return v;
	}

	public String[] getStringList() {
		Vector v = getVectorList();

		String[] s = new String[v.size()];

		for (int i = 0; i < v.size(); i++) {
			s[i] = new String((String) v.elementAt(i));
		}
		return s;
	}

	public String[] getQuotedStringList() throws QuotedStringException {
		return getQuotedStringList(false);
	}

	public String[] getQuotedStringList(boolean return_quotes)
			throws QuotedStringException {
		Vector v = new Vector(10, 10);
		String build_string = " ";

		while (hasMoreElements()) {
			String nextEl = (String) nextElement();
			build_string += nextEl;
			if (build_string.charAt(1) == "'".charAt(0)
					|| build_string.charAt(1) == '"') {
				if (build_string.length() < 3
						|| build_string.charAt(build_string.length() - 1) != build_string
								.charAt(1)) {
					continue;
				}
				if (return_quotes == false) {
					build_string = build_string.substring(1, build_string
							.length() - 1);
				}
			}

			v.addElement(build_string.substring(1));
			build_string = " ";
		}
		if (build_string.length() > 1) {
			throw new QuotedStringException("Missing end quote on"
					+ build_string);
		}

		String[] s = new String[v.size()];

		for (int i = 0; i < v.size(); i++) {
			s[i] = new String((String) v.elementAt(i));
		}
		return s;
	}
}