package software.iridium.api.util;

public interface Hasher {
	String hash(final String source);
	boolean isValid(final String source, final String andHash);
}
