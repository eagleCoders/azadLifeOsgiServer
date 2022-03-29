/**
 * 
 */
package infrastructure.codes.exception;

import infrastructure.core.exception.AbstractPlatformDomainRuleException;

/**
 * @author anees-ur-rehman
 *
 */
public class SystemDefinedCodeCannotBeChangedException extends AbstractPlatformDomainRuleException {
	public SystemDefinedCodeCannotBeChangedException() {
        super("error.msg.code.systemdefined", "This code is system defined and cannot be modified or deleted.");
    }
}
