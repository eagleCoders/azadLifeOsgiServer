/**
 * 
 */
package accounts;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author anees-ur-rehman
 *
 */
@Component(name = "azadlife-account-comp", immediate = true)
public class AccountCamelComponent {

	@Activate
	public void activate(ComponentContext componentContext) throws Exception {
	
	}

}
