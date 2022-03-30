/**
 * 
 */
package accounts.gl.api;

import java.util.HashSet;
import java.util.Set;

/**
 * @author anees-ur-rehman
 *
 */
public enum GLAccountJsonInputParams {

	ID("id"), NAME("name"), PARENT_ID("parentId"), GL_CODE("glCode"), DISABLED("disabled"),
	MANUAL_ENTRIES_ALLOWED("manualEntriesAllowed"), TYPE("type"), USAGE("usage"), DESCRIPTION("description"),
	TAGID("tagId");

	private final String value;

	GLAccountJsonInputParams(final String value) {
		this.value = value;
	}

	private static final Set<String> values = new HashSet<>();

	static {
		for (final GLAccountJsonInputParams type : GLAccountJsonInputParams.values()) {
			values.add(type.value);
		}
	}

	public static Set<String> getAllValues() {
		return values;
	}

	@Override
	public String toString() {
		return name().toString().replaceAll("_", " ");
	}

	public String getValue() {
		return this.value;
	}
}
