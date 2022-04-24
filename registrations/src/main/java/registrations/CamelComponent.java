/**
 * 
 */
package registrations;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.component.activemq.ActiveMQConfiguration;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jpa.JpaComponent;
import org.apache.camel.core.osgi.OsgiClassResolver;
import org.apache.camel.core.osgi.OsgiDataFormatResolver;
import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.apache.camel.core.osgi.OsgiLanguageResolver;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.apache.commons.dbcp2.BasicDataSource;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.TransactionException;
//import org.springframework.transaction.TransactionStatus;

import registrations.routes.AuthenticationProcessor;
import registrations.routes.RegistrationProcessRoutes;

//import org.apache.commons.dbcp.*

/**
 * @author anees-ur-rehman
 *
 */

@Component(name = "azadlife-registration-comp", immediate = true)
public class CamelComponent {

	private ModelCamelContext camelContext;
	private ServiceRegistration<CamelContext> serviceRegistration;

	private EntityManagerFactory emf;
	
	
	@Activate
	public void activate(ComponentContext componentContext) throws Exception {

		Provider provider = new BouncyCastleProvider();
		Security.setProperty("crypto.policy", "unlimited");
		
		int maxKeySize = javax.crypto.Cipher.getMaxAllowedKeyLength("AES");
		System.out.println("Max Key Size for AES : " + maxKeySize);

		Security.addProvider(provider);
		
		BasicDataSource dataSource = new BasicDataSource();
		
		
//		EntityManagerFactory emf = 
//				new HibernatePersistenceProvider().createEntityManagerFactory("azadPayment_unit", Collections.EMPTY_MAP);
//Persistence.createEntityManagerFactory("azadPayment_unit");

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
		osgiDefaultCamelContext.setName("azzad-payments");

		ServiceReference serviceReference =bundleContext.getServiceReference(
                PersistenceProvider.class.getName());
		
		PersistenceProvider persistenceProvider = ( PersistenceProvider ) bundleContext.getService(serviceReference);
		
		emf = persistenceProvider.createEntityManagerFactory(
                "azadPayment_unit",
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
		camelContext.getRegistry().bind("azadPDS", dataSource);
		camelContext.getRegistry().bind("jpa", jpaComponent);
		camelContext.getRegistry().bind("activemq", activeMqComponent);
//		camelContext.getRegistry().bind("PROPAGATION_REQUIRES_NEW", policy);
//		========== Adding JpaComponent to the Registry ==========
		
		serviceRegistration = bundleContext.registerService(CamelContext.class, camelContext, null);
		camelContext.start();
		camelContext.addRoutes(new BaseRoutes());
		camelContext.addRoutes(new RegistrationProcessRoutes());
		camelContext.addRoutes(new AuthenticationProcessor());
	}

	@Deactivate
	public void deactivate() throws Exception {
		camelContext.stop();
		camelContext.removeRouteDefinitions(new ArrayList<RouteDefinition>(camelContext.getRouteDefinitions()));
		serviceRegistration.unregister();
	}

}
