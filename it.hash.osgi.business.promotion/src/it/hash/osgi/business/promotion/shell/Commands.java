package it.hash.osgi.business.promotion.shell;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import it.hash.osgi.business.promotion.Promotion;
import it.hash.osgi.business.promotion.PromotionFactory;
import it.hash.osgi.business.promotion.service.PromotionService;
import it.hash.osgi.utils.StringUtils;

@Component(	immediate = true, 
			service = Commands.class, 
			property = { 
			CommandProcessor.COMMAND_SCOPE + "=promotion",
			CommandProcessor.COMMAND_FUNCTION + "=create", 
			CommandProcessor.COMMAND_FUNCTION + "=delete",
			CommandProcessor.COMMAND_FUNCTION + "=list", 
			CommandProcessor.COMMAND_FUNCTION + "=get" })
public class Commands {
	private volatile PromotionService _promotion;

	@Reference(service = PromotionService.class)
	public void setPromotionService(PromotionService service) {
		_promotion = service;
		System.out.println("Referenced PromotionService: " + (service == null ? "NULL" : "ok"));
	}

	public void unsetPromotionService(PromotionService service) {
		_promotion = null;
	}

	@Activate
	@Modified
	public void activate() {
		System.out.println("Activate Commands Promotions");
	}

	public void create(String type, String businessUuid, String businessName) {
		Promotion promotion = PromotionFactory.getInstance(type);
		promotion.setType(type);
		promotion.setBusinessUuid(businessUuid);
		// promotion.setStartData(start);
		// promotion.setEndData(end);
		promotion.setBusinessName(businessName);
		Map<String, Object> response = _promotion.createPromotion(promotion);

		System.out.println(response.toString());
	}

	public void delete(String uuid, String type) {
		Map<String, Object> list = _promotion.deletePromotion(uuid, type);
		System.out.println(list.get("delete"));
	}

	public void list(String criterion, String search) {
		// Keyword == businessUuid or businessUuid
		List<Promotion> list = null;
		if (StringUtils.isEON(criterion) && StringUtils.isEON(search))
			list = _promotion.getPromotions();
		else
			list = _promotion.retrievepromotions(criterion, search);
		System.out.println("N." + list.size() + " matching promotions");
	}

	public void get(String uuid) {

		List<Promotion> p = _promotion.retrievePromotion(uuid);
		System.out.println(p.toString());

	}

}
