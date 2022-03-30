/**
 * 
 */
package infrastructure.core.serialization;

/**
 * @author anees-ur-rehman
 *
 */
public interface FromApiJsonDeserializer<T> {
	 T commandFromApiJson(String json);
}
