/**
 * 
 */
package accounts.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author anees-ur-rehman
 *
 */
public class EnumOptionData implements Serializable {

	private final Long id;
    private final String code;
    private final String value;

    public EnumOptionData(final Long id, final String code, final String value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    public Long getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnumOptionData)) {
            return false;
        }
        EnumOptionData that = (EnumOptionData) o;
        return id.equals(that.id) && code.equals(that.code) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, value);
    }
}
