package it.hash.osgi.business.promotion.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.promotion.Promotion;


public interface PromotionService {
	Map<String, Object> getBusiness(Map<String, Object> pars);
	
	Map<String, Object> createPromotion(Promotion promotion);
	Map<String, Object> createPromotion(Map<String, Object> pars);
	Map<String, Object> updatePromotion(String uuid, Promotion promotion);
	Map<String, Object> updatePromotion(String uuid, Map<String, Object> pars);
	Map<String, Object> deletePromotion(String uuid);
	Map<String, Object> updatePromotionLogo(String uuid, String type, byte[] encodeBase64);
	Map<String, Object> updatePromotionLogo(String uuid, String type, InputStream istream);
	List<Promotion> retrievePromotion(String search);
	List<Promotion> retrievepromotions(String criterion, String search);
	List<Promotion> getPromotions();
	Promotion getPromotion(String uuid);
	Promotion getPromotion(String uuid, boolean withLogo);
     Map<String, Object> getPromotion(Map<String, Object> pars) ;
		
	
	Promotion getPromotionByBusinessFiscalCode(String BusinessFiscalCode);
//	Promotion getPromotionByBusinessFiscalCode(String BusinessFiscalCode, boolean withLogo);
	Promotion getPromotionByBusinessPartitaIva(String partitaIva);
//	Promotion getPromotionByBusinessPartitaIva(String partitaIva, boolean withLogo);
	Promotion getPromotionByBusinessName(String Name);
	Promotion getPromotionByBusinessName(String Name, boolean withLogo);
	Promotion getPromotionByBusinessId(String businessId);
	Promotion getPromotionByBusinessId(String businessId, boolean withLogo);
	Promotion getPromotionByBusinessUuid(String uuid);
	Promotion getPromotionByBusinessUuid(String uuid, boolean withLogo);
	
	
	
	}