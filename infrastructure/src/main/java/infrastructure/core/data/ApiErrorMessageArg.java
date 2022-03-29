/**
 * 
 */
package infrastructure.core.data;

/**
 * @author anees-ur-rehman
 *
 */
public class ApiErrorMessageArg {

	 /**
     * The actual value of the parameter (if any) as passed to API.
     */
    private Object value;

    public static ApiErrorMessageArg from(final Object object) {
        return new ApiErrorMessageArg(object);
    }

    public ApiErrorMessageArg(final Object object) {
        this.value = object;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }
}
