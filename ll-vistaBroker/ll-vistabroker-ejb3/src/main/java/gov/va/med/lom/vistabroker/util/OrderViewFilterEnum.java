package gov.va.med.lom.vistabroker.util;

public enum OrderViewFilterEnum {
    ALL(1),
	ACTIVE (2),
	DISCONTINUED(3),
	COMPLETE(4),
	EXPIRING(5),
	RECENT(6),
	PENDING(7),
	EXPANDED(8),
	UNVER_NURSE(9),
	UNVER_CLERK(10),
	UNSIGNED(11),
	FLAGGED(12),
	VERBAL_PHONE(13),
	VERBAL_PHONE_UNSIGNED(14),
	HELD(18),
	NEW(19),
	CURRENT(23);

	  private Integer id;
	  
	  
	  private OrderViewFilterEnum(Integer id){
		  this.id = id;
	  }


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}
	  
	  
}
