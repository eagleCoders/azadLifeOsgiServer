/**
 * 
 */
package accounts.gl.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author anees-ur-rehman
 *
 */
public enum GLAccountUsage {

	DETAIL(1, "accountUsage.detail"), HEADER(2, "accountUsage.header");

	private final Integer value;
	private final String code;

	GLAccountUsage(final Integer value, final String code) {
		this.value = value;
		this.code = code;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getCode() {
		return this.code;
	}

	private static final Map<Integer, GLAccountUsage> intToEnumMap = new HashMap<>();
	private static int minValue;
	private static int maxValue;

	static {
		int i = 0;
		for (final GLAccountUsage type : GLAccountUsage.values()) {
			if (i == 0) {
				minValue = type.value;
			}
			intToEnumMap.put(type.value, type);
			if (minValue >= type.value) {
				minValue = type.value;
			}
			if (maxValue < type.value) {
				maxValue = type.value;
			}
			i = i + 1;
		}
	}

	public static GLAccountUsage fromInt(final int i) {
		final GLAccountUsage type = intToEnumMap.get(Integer.valueOf(i));
		return type;
	}

	public static int getMinValue() {
		return minValue;
	}

	public static int getMaxValue() {
		return maxValue;
	}

	@Override
	public String toString() {
		return name().toString();
	}
}