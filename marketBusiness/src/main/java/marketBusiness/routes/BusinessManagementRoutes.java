/**
 * 
 */
package marketBusiness.routes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import marketBusiness.domain.BusinessBranchesBean;
import marketBusiness.domain.BusinessMasterBean;
import marketBusiness.domain.types.BusinessType;
import marketBusiness.domain.types.RegistrationType;

/**
 * @author anees-ur-rehman
 *
 */
public class BusinessManagementRoutes  extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("direct-vm:registerMyBusiness").routeId("direct-vm:registerMyBusiness").log(" Welcome to Business Registration Route").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				BusinessMasterBean bizMasterBean = new BusinessMasterBean();

				Map<String, Object> bodyMap = (Map<String, Object>) exchange.getIn().getBody();
				String businessName =(String) bodyMap.get("businessName");
				String businessType =(String) bodyMap.get("businessType");
				String registrationType =(String) bodyMap.get("registrationType");
				String businessImage =(String) bodyMap.get("businessBanner");
				
				exchange.getIn().setHeader("AZADPAY_BusinessMasterBean", bodyMap);
				
				
				List<Map<String, Object>> branchListMap =(List<Map<String, Object>>) bodyMap.get("businessBranchesList");
				List<BusinessBranchesBean> businessBranchesList = new ArrayList<BusinessBranchesBean>();
				bizMasterBean.setBusinessBranches(businessBranchesList);
				bizMasterBean.setBusinessName(businessName);
				bizMasterBean.setBusinessType(BusinessType.valueOf(businessType));
				bizMasterBean.setRegistrationType(RegistrationType.valueOf(registrationType));
				bizMasterBean.setBusinessBanner(businessImage);
				
				branchListMap.stream().forEach(e->{
					BusinessBranchesBean businessBranchesBean = new BusinessBranchesBean();
					
					Integer brnachPosition = (Integer) e.get("branchPosition");
					Boolean isHQBranch = (Boolean) e.get("hqBranch");
					String branchName = (String) e.get("branchName");
					String branchAddress = (String) e.get("branchAddress");
					Integer branchLogitude = (Integer) e.get("longitutde");
					Integer branchLatitude = (Integer) e.get("latitude");
					
					businessBranchesBean.setBranchPosition(brnachPosition);
					businessBranchesBean.setPrimaryBranch(isHQBranch);
					businessBranchesBean.setBranchAdress(branchAddress);
					businessBranchesBean.setBranchName(branchName);
					businessBranchesBean.setBusinessLatitude(Long.valueOf(branchLatitude));
					businessBranchesBean.setBusinessLongitude(Long.valueOf(branchLogitude));
					
					businessBranchesList.add(businessBranchesBean);
					
					exchange.getIn().setBody(bizMasterBean);
					
				});
				
			}
		}).transacted().toD("jpa://" + BusinessMasterBean.class.getName() + "?usePersist=true")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				Map<String, Object> bodyMap = (Map<String, Object>) exchange.getIn().getHeader("AZADPAY_BusinessMasterBean");
				System.out.println("[TalkToMeRoutes] : AZADPAY_BusinessMasterBean : "+bodyMap);
				exchange.getIn().removeHeader("AZADPAY_BusinessMasterBean");
				exchange.getIn().setBody(bodyMap);
			}
		})
		.wireTap("direct-vm:updateBusinessRegistrationToAdmin");
	}

}
