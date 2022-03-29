/**
 * 
 */
package infrastructure.codes.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

import infrastructure.codes.CodeConstants.CodevalueJSONinputParams;
import infrastructure.codes.data.CodeValueData;
import infrastructure.core.api.JsonCommand;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name = "m_code_value", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "code_id", "code_value" }, name = "code_value_duplicate") })
@SequenceGenerator(name = "azlife_infra_cd_value", sequenceName = "az_infro_cd_val")
public class CodeValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "azlife_infra_cd_value")
	private Long id;

	@Column(name = "code_value", length = 100)
	private String label;

	@Column(name = "order_position")
	private int position;

	@Column(name = "code_description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "code_id", nullable = false)
	private Code code;

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "is_mandatory")
	private boolean mandatory;

	public static CodeValue createNew(final Code code, final String label, final int position, final String description,
			final boolean isActive, final boolean mandatory) {
		return new CodeValue(code, label, position, description, isActive, mandatory);
	}

	protected CodeValue() {
		//
	}

	private CodeValue(final Code code, final String label, final int position, final String description,
			final boolean isActive, final boolean mandatory) {
		this.code = code;
		this.label = StringUtils.defaultIfEmpty(label, null);
		this.position = position;
		this.description = description;
		this.isActive = isActive;
		this.mandatory = mandatory;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public static CodeValue fromJson(final Code code, final JsonCommand command) {

		final String label = command.stringValueOfParameterNamed(CodevalueJSONinputParams.NAME.getValue());
		Integer position = command.integerValueSansLocaleOfParameterNamed(CodevalueJSONinputParams.POSITION.getValue());
		String description = command.stringValueOfParameterNamed(CodevalueJSONinputParams.DESCRIPTION.getValue());
		Boolean isActiveObj = command.booleanObjectValueOfParameterNamed(CodevalueJSONinputParams.IS_ACTIVE.getValue());
		boolean isActive = true;
		if (isActiveObj != null) {
			isActive = isActiveObj;
		}
		if (position == null) {
			position = 0;
		}
		Boolean mandatory = command
				.booleanPrimitiveValueOfParameterNamed(CodevalueJSONinputParams.IS_MANDATORY.getValue());

		return new CodeValue(code, label, position, description, isActive, mandatory);
	}
	
	public Map<String, Object> update(final JsonCommand command) {

        final Map<String, Object> actualChanges = new LinkedHashMap<>(2);

        final String labelParamName = CodevalueJSONinputParams.NAME.getValue();
        if (command.isChangeInStringParameterNamed(labelParamName, this.label)) {
            final String newValue = command.stringValueOfParameterNamed(labelParamName);
            actualChanges.put(labelParamName, newValue);
            this.label = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String decriptionParamName = CodevalueJSONinputParams.DESCRIPTION.getValue();
        if (command.isChangeInStringParameterNamed(decriptionParamName, this.description)) {
            final String newValue = command.stringValueOfParameterNamed(decriptionParamName);
            actualChanges.put(decriptionParamName, newValue);
            this.description = StringUtils.defaultIfEmpty(newValue, null);
        }

        final String positionParamName = CodevalueJSONinputParams.POSITION.getValue();
        if (command.isChangeInIntegerSansLocaleParameterNamed(positionParamName, this.position)) {
            final Integer newValue = command.integerValueSansLocaleOfParameterNamed(positionParamName);
            actualChanges.put(positionParamName, newValue);
            this.position = newValue;
        }

        final String isActiveParamName = CodevalueJSONinputParams.IS_ACTIVE.getValue();
        if (command.isChangeInBooleanParameterNamed(isActiveParamName, this.isActive)) {
            final Boolean newValue = command.booleanPrimitiveValueOfParameterNamed(isActiveParamName);
            actualChanges.put(isActiveParamName, newValue);
            this.isActive = newValue;
        }

        return actualChanges;
    }

	public CodeValueData toData() {
		return CodeValueData.instance(getId(), this.label, this.position, this.isActive, this.mandatory);
	}

}
