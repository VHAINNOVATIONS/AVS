package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.Address;

public class AddressDao extends BaseDao {
  
  // CONSTRUCTORS
  public AddressDao() {
    super();
  }
  
  public AddressDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  // RPC API
  public Address getAddress(String dfn) throws Exception {
    Address address = null ;  
    setDefaultContext("ALS CLINICAL RPC");
    setDefaultRpcName("ALSI ADDRESS LOOKUP");
    String x = sCall(dfn);
    if (x.length() > 0) {
      address = new Address(); 
      address.setDfn(dfn);
      address.setStreet1(StringUtils.piece(x, 3));
      address.setStreet2(StringUtils.piece(x, 4));
      address.setStreet3(StringUtils.piece(x, 5));
      address.setCity(StringUtils.piece(x, 6));
      address.setStateNumber(StringUtils.toInt(StringUtils.piece(x, 7), 0));
      address.setState(StringUtils.piece(x, 8));
      address.setZipCode(StringUtils.piece(x, 9));
      address.setCountyNumber(StringUtils.toInt(StringUtils.piece(x, 10), 0));
      address.setCounty(StringUtils.piece(x, 11));
      address.setPhoneNumber(StringUtils.piece(x, 12));
      address.setFlagNumber(StringUtils.toInt(StringUtils.piece(x, 13), 0));
      address.setFlag(StringUtils.piece(x, 14));
      address.setEmail(StringUtils.piece(x, 15));
    }
    return address;
  }
  
}