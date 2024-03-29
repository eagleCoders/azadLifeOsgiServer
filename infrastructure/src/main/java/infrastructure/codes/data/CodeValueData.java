/**
 * 
 */
package infrastructure.codes.data;

import java.io.Serializable;

/**
 * @author anees-ur-rehman
 *
 */
public class CodeValueData implements Serializable {
	private final Long id;

	private final String name;

	@SuppressWarnings("unused")
	private final Integer position;

	@SuppressWarnings("unused")
	private final String description;
	private final boolean active;
	private final boolean mandatory;

	public CodeValueData(final Long id) {
		this.id = id;
		this.name = null;
		this.position = null;
		this.description = null;
		this.active = false;
		this.mandatory = false;
	}

	public static CodeValueData instance(final Long id, final String name, final Integer position,
			final boolean isActive, final boolean mandatory) {
		String description = null;
		return new CodeValueData(id, name, position, description, isActive, mandatory);
	}

	public static CodeValueData instance(final Long id, final String name, final String description,
			final boolean isActive, final boolean mandatory) {
		Integer position = null;
		return new CodeValueData(id, name, position, description, isActive, mandatory);
	}

	public static CodeValueData instance(final Long id, final String name, final String description,
			final boolean isActive) {
		Integer position = null;
		boolean mandatory = false;

		return new CodeValueData(id, name, position, description, isActive, mandatory);
	}

	public static CodeValueData instance(final Long id, final String name) {
		String description = null;
		Integer position = null;
		boolean isActive = false;
		boolean mandatory = false;

		return new CodeValueData(id, name, position, description, isActive, mandatory);
	}

	public static CodeValueData instance(final Long id, final String name, final Integer position,
			final String description, final boolean isActive, final boolean mandatory) {
		return new CodeValueData(id, name, position, description, isActive, mandatory);
	}

	private CodeValueData(final Long id, final String name, final Integer position, final String description,
			final boolean active, final boolean mandatory) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.description = description;
		this.active = active;
		this.mandatory = mandatory;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return the mandatory
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
}
