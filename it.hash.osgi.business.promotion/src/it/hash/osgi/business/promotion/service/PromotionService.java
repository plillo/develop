package it.hash.osgi.business.promotion.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.promotion.Promotion;


public interface PromotionService {
	
	Map<String, Object> createPromotion(Promotion promotion);
	Map<String, Object> createPromotion(Map<String, Object> pars);
	

	List<Promotion> retrievePromotion(String search);
	List<Promotion> retrievepromotions(String criterion, String search);
	List<Promotion> getPromotions();
	Promotion getPromotion(String uuid);
	Promotion getPromotion(String uuid, boolean withLogo);
    Map<String, Object> getPromotion(Map<String, Object> pars) ;
	List<Promotion> getPromotionsByBusinessUuid(String businessUuid);
     
	Map<String, Object> updatePromotion(Promotion promotion);
	Map<String, Object> updatePromotion(String uuid, Map<String, Object> pars);
	Map<String, Object> updatePromotionLogo(String uuid, String type, byte[] encodeBase64);
	Map<String, Object> updatePromotionLogo(String uuid, String type, InputStream istream);
	
	
	Map<String, Object> deletePromotion(String uuid,String type);
	
	
//	Promotion getPromotionByBusinessFiscalCode(String BusinessFiscalCode);
//	Promotion getPromotionByBusinessFiscalCode(String BusinessFiscalCode, boolean withLogo);
//	Promotion getPromotionByBusinessPartitaIva(String partitaIva);
//	Promotion getPromotionByBusinessPartitaIva(String partitaIva, boolean withLogo);
//	Promotion getPromotionByBusinessName(String Name);
//	Promotion getPromotionByBusinessName(String Name, boolean withLogo);
//	Promotion getPromotionByBusinessId(String businessId);
	//Promotion getPromotionByBusinessId(String businessId, boolean withLogo);

//	Promotion getPromotionByBusinessUuid(String uuid, boolean withLogo);
	
	
	
	}
