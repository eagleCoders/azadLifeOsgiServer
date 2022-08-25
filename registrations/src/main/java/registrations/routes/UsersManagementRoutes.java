/**
 * 
 */
package registrations.routes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import registrations.domain.UserLoggedInLocation;
import registrations.domain.UserRegistrationBean;

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
		
		
		from("direct-vm:updateLoggedInLocation").routeId("direct-vm_updateLoggedInLocation").log("LoggedInUserLocation Updates")
		.to("direct:loadCurrentUserDetails").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				List<Map<String, Object>> userDataMapList =(List<Map<String, Object>>) exchange.getIn().getBody();
				Map<String, Object> userDetailMap = userDataMapList.get(0);
				Map<String, Object> userLocationMap =(Map<String, Object>) exchange.getIn().getHeader("AzadLife_userLocationDataMap");
				String globalId =(String) userDetailMap.get("globalid");
				Double latitude =(Double) userLocationMap.get("latitude");
				Double longitude =(Double) userLocationMap.get("longitude");
				Long loggedInDate =(Long)userLocationMap.get("loginDate");
				Date date = new Date(loggedInDate);
				LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				UserLoggedInLocation userLocationBean = new UserLoggedInLocation();
				userLocationBean.setGlobalId(globalId);
				userLocationBean.setLatitude(latitude);
				userLocationBean.setLongitude(longitude);
				userLocationBean.setLoginDate(currentDate);

				exchange.getIn().setBody(userLocationBean);

				
			}
		}).transacted().toD("jpa://" + UserLoggedInLocation.class.getName() + "?usePersist=true").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {

				UserLoggedInLocation userLoggedInLocationBean =(UserLoggedInLocation) exchange.getIn().getBody();
				
				Map<String, Object> userLocationMap =(Map<String, Object>) exchange.getIn().getHeader("AzadLife_userLocationDataMap");
				userLocationMap.put("userLocationId", userLoggedInLocationBean.getId());
				userLocationMap.put("globalId", userLoggedInLocationBean.getGlobalId());
				exchange.getIn().setBody(userLocationMap);
				exchange.getIn().removeHeader("AzadLife_userLocationDataMap");

			}
		}).marshal().json(JsonLibrary.Gson).convertBodyTo(String.class);
		
		from("direct:loadCurrentUserDetails").routeId("direct:loadCurrentUserDetails").log("Fetching User Details for UserLocation Updates").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				Map<String, Object> userLocationMap =(Map<String, Object>) exchange.getIn().getBody();
				String userName =(String) userLocationMap.get("userName");
				userLocationMap.get("latitude");
				userLocationMap.get("longitude");
				
				exchange.getIn().setHeader("AzadLife_userLocationDataMap", userLocationMap);

				StringBuilder selectStatement = new StringBuilder();
				selectStatement
						.append("select user_name, user_password, user_role ,globalid ,email from user_registration where user_name = '"+userName+"'");
				exchange.getIn().setBody(selectStatement.toString());	

			}
		}).to("jdbc:azadPDS");
		
	}

}
