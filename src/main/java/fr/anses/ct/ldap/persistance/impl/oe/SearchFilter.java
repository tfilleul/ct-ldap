package fr.anses.ct.ldap.persistance.impl.oe;

public class SearchFilter {

	/**
	 * Base de DN de recherche
	 */
	private String baseDn ; 
	
	/**
	 * Filtre de recherche
	 */
	private String filter;

	/**
	 * @return the baseDn
	 */
	public String getBaseDn() {
		return baseDn;
	}

	/**
	 * @param baseDn the baseDn to set
	 */
	public void setBaseDn(String baseDn) {
		this.baseDn = baseDn;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	
}
