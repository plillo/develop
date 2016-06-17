package it.hash.osgi.business.promotion.shell;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.business.promotion.Promotion;
import it.hash.osgi.business.promotion.PromotionFactory;
import it.hash.osgi.business.promotion.service.PromotionService;

@Component(
		immediate=true, 
		service = Commands.class, 
		property = {
			CommandProcessor.COMMAND_SCOPE+"=promotion",
			CommandProcessor.COMMAND_FUNCTION+"=create",
			CommandProcessor.COMMAND_FUNCTION+"=delete",
			CommandProcessor.COMMAND_FUNCTION+"=list",
			CommandProcessor.COMMAND_FUNCTION+"=get"}
	)
public class Commands {
	private volatile PromotionService _promotion;

	
	@Reference(service=PromotionService.class)
	public void setPromotionService(PromotionService service){
		_promotion = service;
		System.out.println("Referenced PromotionService: "+(service==null?"NULL":"ok"));
	}
	
	public void unsetUUIDService(PromotionService service){
		_promotion = null;
	}
	
	
	public void create(String type, String businessUuid, String businessName) {
		Promotion promotion =PromotionFactory.getInstance(type);
		promotion.setType(type);
		promotion.setBusinessUuid(businessUuid);
		//promotion.setStartData(start);
		//promotion.setEndData(end);
		promotion.setBusinessName(businessName);
		Map<String, Object> response = _promotion.createPromotion(promotion);
		
		System.out.println(response.toString());
	}
	
	public void delete(String uuid) {
		Map<String, Object> list = _promotion.deletePromotion(uuid);
		
		System.out.println(list.get("deleted"));
	}
	
	public void list(String keyword) {
		List<Promotion> list = _promotion.retrievepromotions(keyword, keyword);
		
		System.out.println("N."+list.size()+" matching products");
	}
	
	
	public void get(String uuid){
		
		Promotion p = _promotion.getPromotion(uuid);
		System.out.println(p.toString());
		
	}

	
}
