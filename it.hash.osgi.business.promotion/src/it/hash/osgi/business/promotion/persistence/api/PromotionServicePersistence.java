/**
 * Persistence Api
 * @author Montinari Antonella
 */
package it.hash.osgi.business.promotion.persistence.api;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.promotion.Promotion;




/**
 * Provides interfaces for the management of the persistence of a Promotion
 * @author Montinari Antonella
 */
public interface PromotionServicePersistence {
	// CREATE
	Map<String, Object> addPromotion(Map<String, Object> promotion);
	Map<String, Object> addPromotion(Promotion promotion);
	             
	// READ
	Map<String, Object> getPromotion(Promotion promotion);
	Map<String, Object> getPromotion(Map<String, Object> promotion);
	Map<String, Object> getPromotion(Promotion promotion, boolean withLogo);
	Promotion getPromotionByUuid(String uuid);
	List<Promotion> getPromotions();
//	Promotion getPromotionByName(String Name);

	

	
	List<Promotion> retrievePromotions(Map<String,Object> mapKeywords);
	List<Promotion> retrievePromotions(String keyword);
	List<Promotion> retrievePromotions(String keyword,String search);
	List<Promotion> getPromotionByBusinessUuid(String uuid);
	
//	List<Promotion> retrievePromotionsByBusinessFiscalCode(String BusinessFiscalCode);
//	Promotion getPromotionByBusinessFiscalCode(String BusinessFiscalCode, boolean withLogo);
//	List<Promotion> getPromotionByBusinessPartitaIva(String partitaIva);
//	Promotion getPromotionByBusinessPartitaIva(String partitaIva, boolean withLogo);
/*	List<Promotion> getPromotionByBusinessName(String Name);
	List<Promotion> getPromotionByBusinessName(String Name, boolean withLogo);
	List<Promotion> getPromotionByBusinessId(String businessId);
	List<Promotion> getPromotionByBusinessId(String businessId, boolean withLogo);

	List<Promotion> getPromotionByBusinessUuid(String uuid, boolean withLogo);
	List<Promotion> getPromotionByType(String type);*/
	//elenco delle promozioni scadute, in corso , tra due date ,
	//che scadono in un particolare giorno
	// elenco delle promozioni su un determinato prodotto
	// elenco delle promozioni su un determinato prodotto e in base al tipo di promozione 
	// elenco delle promozioni su un determinato prodotto di u determinato business 
	
	
	
	
	
	// UPDATE
	Map<String, Object> updatePromotion(Promotion promotion);
	Map<String, Object> updatePromotion(String uuid, Map<String, Object> promotion);
	
	// DELETE
	Map<String, Object> deletePromotion(String uuid,String type);
	
	// IMPLEMENTATION
	String getImplementation();


}
