/**
 * 
 */
package talktome;

import java.util.ArrayList;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.component.activemq.ActiveMQConfiguration;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.core.osgi.OsgiClassResolver;
import org.apache.camel.core.osgi.OsgiDataFormatResolver;
import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.apache.camel.core.osgi.OsgiLanguageResolver;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import talktome.routes.TalkToMeRoutes;

/**
 * @author anees-ur-rehman
 *
 */
@Component(name = "azadlife-talktome-comp", immediate = true)
public class TalkToMeComponent {

	private ModelCamelContext camelContext;
	private ServiceRegistration<CamelContext> serviceRegistration;

	@Activate
	public void activate(ComponentContext componentContext) throws Exception {
		
		BundleContext bundleContext = componentContext.getBundleContext();
		OsgiDefaultCamelContext osgiDefaultCamelContext = new OsgiDefaultCamelContext(bundleContext);
		osgiDefaultCamelContext.setClassResolver(new OsgiClassResolver(camelContext, bundleContext));
		osgiDefaultCamelContext.setDataFormatResolver(new OsgiDataFormatResolver(bundleContext));
		osgiDefaultCamelContext.setLanguageResolver(new OsgiLanguageResolver(bundleContext));
		osgiDefaultCamelContext.setName("azzad-talktome");

		
//		=========== ActiveMQ Configuration ================
		ActiveMQConfiguration activeMQConfiguration = new ActiveMQConfiguration();
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL("tcp://0.0.0.0:61616");
		activeMQConnectionFactory.setUserName("admin");
		activeMQConnectionFactory.setPassword("admin");
		activeMQConnectionFactory.setExclusiveConsumer(true);
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
		pooledConnectionFactory.setMaxConnections(15);
		pooledConnectionFactory.setExpiryTimeout(-1);
		pooledConnectionFactory.setIdleTimeout(-1);
		
		JmsConfiguration jmsConfiguration  = new JmsConfiguration();
		jmsConfiguration.setConnectionFactory(pooledConnectionFactory);
		jmsConfiguration.setConcurrentConsumers(50);
		
		ActiveMQComponent activeMqComponent = new ActiveMQComponent();
		activeMqComponent.setConfiguration(jmsConfiguration);

		camelContext = osgiDefaultCamelContext;
		camelContext.getRegistry().bind("activemq", activeMqComponent);

		serviceRegistration = bundleContext.registerService(CamelContext.class, camelContext, null);
		camelContext.start();

		camelContext.addRoutes(new TalkToMeRoutes());

	}
	
	@Deactivate
	public void deactivate() throws Exception {
		camelContext.stop();
		camelContext.removeRouteDefinitions(new ArrayList<RouteDefinition>(camelContext.getRouteDefinitions()));
		serviceRegistration.unregister();
	}

}
