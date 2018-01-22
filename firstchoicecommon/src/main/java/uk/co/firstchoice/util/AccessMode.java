package uk.co.firstchoice.util;

/**
 * class AccessMode This is a typesafe enum pattern used to define the access
 * mode of this application
 */
public class AccessMode {
	// type safe enum pattern

	private final String name;

	private AccessMode(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public static final AccessMode CLIENT = new AccessMode("Client mode");

	public static final AccessMode JSERVER = new AccessMode(
			"Database JServer mode");

	public static final AccessMode FAILED = new AccessMode(
			"Access Error - No connection");
}

