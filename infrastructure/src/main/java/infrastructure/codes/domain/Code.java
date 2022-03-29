/**
 * 
 */
package infrastructure.codes.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

import infrastructure.codes.exception.SystemDefinedCodeCannotBeChangedException;
import infrastructure.core.api.JsonCommand;

/**
 * @author anees-ur-rehman
 *
 */
@Entity
@Table(name = "m_code", uniqueConstraints = { @UniqueConstraint(columnNames = { "code_name" }, name = "code_name") })
@SequenceGenerator(name="azlife_infra_cd", sequenceName="az_infro_cd")
public class Code implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "azlife_infra_cd")
	private Long id;
	
	@Column(name = "code_name", length = 100)
    private String name;

    @Column(name = "is_system_defined")
    private boolean systemDefined;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "code", orphanRemoval = true)
    private Set<CodeValue> values;
    
    public static Code fromJson(final JsonCommand command) {
        final String name = command.stringValueOfParameterNamed("name");
        return new Code(name);
    }
    
    protected Code() {
        this.systemDefined = false;
    }

    private Code(final String name) {
        this.name = name;
        this.systemDefined = false;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSystemDefined() {
		return systemDefined;
	}

	public void setSystemDefined(boolean systemDefined) {
		this.systemDefined = systemDefined;
	}

	public Set<CodeValue> getValues() {
		return values;
	}

	public void setValues(Set<CodeValue> values) {
		this.values = values;
	}
	
	public Map<String, Object> update(final JsonCommand command) {

        if (this.systemDefined) {
            throw new SystemDefinedCodeCannotBeChangedException();
        }

        final Map<String, Object> actualChanges = new LinkedHashMap<>(1);

        final String firstnameParamName = "name";
        if (command.isChangeInStringParameterNamed(firstnameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(firstnameParamName);
            actualChanges.put(firstnameParamName, newValue);
            this.name = StringUtils.defaultIfEmpty(newValue, null);
        }

        return actualChanges;
    }

    public boolean remove(final CodeValue codeValueToDelete) {
        return this.values.remove(codeValueToDelete);
    }

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Transient
    private boolean isNew = true;

    public Long getId() {
        return id;
    }

    protected void setId(final Long id) {
        this.id = id;
    }

    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }


}
