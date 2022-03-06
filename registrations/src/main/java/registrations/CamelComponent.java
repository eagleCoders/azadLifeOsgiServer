/**
 * 
 */
package registrations;

import java.util.ArrayList;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
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

//import org.apache.commons.dbcp.*

/**
 * @author anees-ur-rehman
 *
 */

@Component(name = "azadlife-registration-comp", immediate = true)
public class CamelComponent {

	private ModelCamelContext camelContext;
	private ServiceRegistration<CamelContext> serviceRegistration;

	@Activate
	public void activate(ComponentContext componentContext) throws Exception {

//		SimpleRegistry registry = new SimpleRegistry();
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setUrl("jdbc:postgresql://localhost:5432/testingDB");
//		dataSource.setUsername("postgres");
//		dataSource.setPassword("madho1431");

//		registry.bind("azadDS", dataSource);

		BundleContext bundleContext = componentContext.getBundleContext();
		OsgiDefaultCamelContext osgiDefaultCamelContext = new OsgiDefaultCamelContext(bundleContext);
		osgiDefaultCamelContext.setClassResolver(new OsgiClassResolver(camelContext, bundleContext));
		osgiDefaultCamelContext.setDataFormatResolver(new OsgiDataFormatResolver(bundleContext));
		osgiDefaultCamelContext.setLanguageResolver(new OsgiLanguageResolver(bundleContext));
		osgiDefaultCamelContext.setName("context-example");

//		osgiDefaultCamelContext.getCamelContextReference().getRegistry().bind("azadDS", dataSource);

		camelContext = osgiDefaultCamelContext;
		serviceRegistration = bundleContext.registerService(CamelContext.class, camelContext, null);
		camelContext.start();
		camelContext.addRoutes(new BaseRoutes());
	}

	@Deactivate
	public void deactivate() throws Exception {
		camelContext.stop();
		camelContext.removeRouteDefinitions(new ArrayList<RouteDefinition>(camelContext.getRouteDefinitions()));
		serviceRegistration.unregister();
	}

}
