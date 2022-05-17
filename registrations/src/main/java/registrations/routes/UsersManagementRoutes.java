/**
 * 
 */
package registrations.routes;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import registrations.domain.UserRegistrationBean;
import registrations.domain.UserTypeChangeBean;

/**
 * @author anees-ur-rehman
 *
 */
public class UsersManagementRoutes extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct-vm:loadSelectedUser").routeId("direct-vm:loadSelectedUser").transacted().process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				String userName =(String) exchange.getIn().getHeader("userName");
				UserRegistrationBean userRegistrationBean = new UserRegistrationBean();
				userRegistrationBean.setUserName(userName);
				exchange.getIn().setHeader("AZADPAY_USERID", userName);
				
			}
		}).toD("jpa://" + UserRegistrationBean.class.getName() + "?resultClass="
				+ UserRegistrationBean.class.getName() + "&query=select b from "
				+ UserRegistrationBean.class.getName() + " b where b.userName = '${header.AZADPAY_USERID}'")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				List<UserRegistrationBean> listMap = (List<UserRegistrationBean>) exchange.getIn().getBody();	
				UserRegistrationBean userRegistrationBean = 	listMap.get(0);
				exchange.getIn().setBody(userRegistrationBean);
			}
		}).marshal().json(JsonLibrary.Gson).convertBodyTo(String.class);
	}

}
