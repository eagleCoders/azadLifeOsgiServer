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
 * 5229583097:AAF9NQJn8NK-jlY2bQCMSTjCqkyWuj_Nlo8
 *
 */
@Component(name = "azadlife-telegrambot-comp", immediate = true)
public class CamelTelegramBotComponent {
	
	
	@Activate
	public void activate(ComponentContext componentContext) throws Exception {
	
	}

}
