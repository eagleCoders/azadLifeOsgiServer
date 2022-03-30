/**
 * 
 */
package infrastructure.core.serialization;

/**
 * @author anees-ur-rehman
 *
 */
public abstract class AbstractFromApiJsonDeserializer<T> implements FromApiJsonDeserializer<T> {

	@Override
    public abstract T commandFromApiJson(String json);
}
