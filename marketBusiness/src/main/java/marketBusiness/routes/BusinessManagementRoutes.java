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
				
				
				List<Map<String, String>> branchListMap =(List<Map<String, String>>) bodyMap.get("businessBranchesList");
				List<BusinessBranchesBean> businessBranchesList = new ArrayList<BusinessBranchesBean>();
				bizMasterBean.setBusinessBranches(businessBranchesList);
				bizMasterBean.setBusinessName(businessName);
				bizMasterBean.setBusinessType(BusinessType.valueOf(businessType));
				bizMasterBean.setRegistrationType(RegistrationType.valueOf(registrationType));
				branchListMap.stream().forEach(e->{
					BusinessBranchesBean businessBranchesBean = new BusinessBranchesBean();
					
					String brnachPosition = (String) e.get("branchPosition");
					String isHQBranch = (String) e.get("hqBranch");
					String branchName = (String) e.get("branchName");
					String branchAddress = (String) e.get("branchAddress");
					String branchLogitude = (String) e.get("longitutde");
					String branchLatitude = (String) e.get("latitude");
					
					businessBranchesBean.setBranchPosition(Integer.valueOf(brnachPosition));
					businessBranchesBean.setPrimaryBranch(Boolean.valueOf(isHQBranch));
					businessBranchesBean.setBranchAdress(branchAddress);
					businessBranchesBean.setBranchName(branchName);
					businessBranchesBean.setBusinessLatitude(Long.valueOf(branchLatitude));
					businessBranchesBean.setBusinessLongitude(Long.valueOf(branchLogitude));
					
					businessBranchesList.add(businessBranchesBean);
				});
				
			}
		}).transacted().toD("jpa://" + BusinessMasterBean.class.getName() + "?usePersist=true").to("direct-vm:updateBusinessRegistrationToAdmin");
	}

}
