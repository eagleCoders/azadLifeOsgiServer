/**
 * 
 */
package socialmediabots;

import org.apache.camel.component.telegram.TelegramComponent;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author anees-ur-rehman
 *
 */
@Component(name = "azadlife-telegrambot-comp", immediate = true)
public class CamelTelegramBotComponent {
	
	
	@Activate
	public void activate(ComponentContext componentContext) throws Exception {
	
		TelegramComponent telegramComponent = new TelegramComponent();
	}

}
