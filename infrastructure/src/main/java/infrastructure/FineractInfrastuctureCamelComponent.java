/**
 * 
 */
package infrastructure;

import java.util.ArrayList;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jpa.JpaComponent;
import org.apache.camel.core.osgi.OsgiClassResolver;
import org.apache.camel.core.osgi.OsgiDataFormatResolver;
import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.apache.camel.core.osgi.OsgiLanguageResolver;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.apache.commons.dbcp2.BasicDataSource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * @author anees-ur-rehman
 *
 */
@Component(name = "fineract-infrascture-comp", immediate = true)
public class FineractInfrastuctureCamelComponent {

	private ModelCamelContext camelContext;
	private ServiceRegistration<CamelContext> serviceRegistration;

	private EntityManagerFactory emf;
	
	
	@Activate
	public void activate(ComponentContext componentContext) throws Exception {
	
		BasicDataSource dataSource = new BasicDataSource();
		
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/azadPayments");
		dataSource.setUsername("ubuntu");
		dataSource.setPassword("asanlife_#");

//		dataSource.setUsername("postgres");
//		dataSource.setPassword("madho1431");
		
		BundleContext bundleContext = componentContext.getBundleContext();
		
		OsgiDefaultCamelContext osgiDefaultCamelContext = new OsgiDefaultCamelContext(bundleContext);
		osgiDefaultCamelContext.setClassResolver(new OsgiClassResolver(camelContext, bundleContext));
		osgiDefaultCamelContext.setDataFormatResolver(new OsgiDataFormatResolver(bundleContext));
		osgiDefaultCamelContext.setLanguageResolver(new OsgiLanguageResolver(bundleContext));
		osgiDefaultCamelContext.setName("fineract-infrastructure");
		
		ServiceReference serviceReference =bundleContext.getServiceReference(
                PersistenceProvider.class.getName());
		
		PersistenceProvider persistenceProvider = ( PersistenceProvider ) bundleContext.getService(serviceReference);
		
		emf = persistenceProvider.createEntityManagerFactory(
                "fineractInfra_unit",
                null
            );

		JpaComponent jpaComponent = new JpaComponent();
		jpaComponent.setEntityManagerFactory(emf);
		jpaComponent.setJoinTransaction(true);
		JpaTransactionManager jpaTranx = new JpaTransactionManager();
		jpaTranx.setEntityManagerFactory(emf);
		jpaComponent.setTransactionManager(jpaTranx);
		
		SpringTransactionPolicy policy = new SpringTransactionPolicy ();
		policy.setTransactionManager(jpaTranx);
		policy.setPropagationBehaviorName("PROPAGATIO‌​N_REQUIRES_NEW");
//	    policy = policy.setTransactionManager(jpaTranx);
//	    policy.setPropagationBehav‌​iourName("PROPAGATIO‌​N_REQUIRES_NEW");
		
		camelContext = osgiDefaultCamelContext;
		camelContext.getRegistry().bind("azadPDS", dataSource);
		camelContext.getRegistry().bind("jpa", jpaComponent);
//		camelContext.getRegistry().bind("PROPAGATION_REQUIRES_NEW", policy);
//		========== Adding JpaComponent to the Registry ==========
		
		serviceRegistration = bundleContext.registerService(CamelContext.class, camelContext, null);
		camelContext.start();


	}
	
	@Deactivate
	public void deactivate() throws Exception {
		camelContext.stop();
		camelContext.removeRouteDefinitions(new ArrayList<RouteDefinition>(camelContext.getRouteDefinitions()));
		serviceRegistration.unregister();
	}
}
