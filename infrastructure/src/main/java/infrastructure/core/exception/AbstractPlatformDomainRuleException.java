/**
 * 
 */
package infrastructure.core.exception;

/**
 * @author anees-ur-rehman
 *
 */
public abstract class AbstractPlatformDomainRuleException extends AbstractPlatformException {
	protected AbstractPlatformDomainRuleException(String globalisationMessageCode, String defaultUserMessage,
            Object... defaultUserMessageArgs) {
        super(globalisationMessageCode, defaultUserMessage, defaultUserMessageArgs);
    }
}
