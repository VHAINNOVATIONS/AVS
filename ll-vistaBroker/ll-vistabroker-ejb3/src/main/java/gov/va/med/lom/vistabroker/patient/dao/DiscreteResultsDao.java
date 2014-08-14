package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.patient.data.DiscreteItemData;
import gov.va.med.lom.vistabroker.util.FMDateUtils;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.HashMap;

public class DiscreteResultsDao extends BaseDao {

	// these represent fileNum's and names returned by ORWGRPC TYPES ["0", "0"]
	public static final String LAB_TESTS_FILENUM = "63";
	public static final String VITALS_FILENUM = "120.5";
	public static final String PROBLEMS_FILENUM = "9000011";
	public static final String ANATOMIC_PATHOLOGY_FILENUM = "63AP";
	public static final String MICROBIOLOGY_FILENUM = "63MI";
	public static final String BLOOD_BANK_FILENUM = "63BB";
	public static final String MEDICINE_FILENUM = "690";

    private HashMap<String, List<String>> itemCache = new HashMap<String, List<String>>();

	// CONSTRUCTORS
    public DiscreteResultsDao() {
    	super();
    }
  
    public DiscreteResultsDao(BaseDao baseDao) {
    	super(baseDao);
    }
    
    public String lookupItemNum(String dfn, String fileNum, String itemName) throws Exception {
    	
    	// retrieve RPC data (lazy load from cache)
    	String key = dfn + '^' + fileNum;
	    List<String> itemList = null;
    	if (!this.itemCache.containsKey(key)) {
        	setDefaultContext("OR CPRS GUI CHART");
        	setDefaultRpcName("ORWGRPC ITEMS");
        	Object[] params = {dfn, fileNum};
        	List<String> list = lCall(params);
	    	this.itemCache.put(key, list);
	    }
    	itemList = this.itemCache.get(key);
    	
    	// look through list for the given itemName
    	String itemNum = null;
    	for (String s : itemList) {
    		if (StringUtils.piece(s, 4).equals(itemName)) {
    			itemNum = StringUtils.piece(s, 2);
    			break;
    		}
    	}
    	return itemNum;
    }

    /**
     * List of item data
     * @param dfn Patient DFN
     * @param typeNum Type number (file number) in which to look
     * @param itemNum Item number (file number) from which to retrieve data
     * @param fmStartDate First date from which to return data
     * @return List of data records, or zero-size List if none were found
     */
    public List<DiscreteItemData> getItemDataSet(String dfn, String typeNum, String itemNum, double fmStartDate) throws Exception {
    	    	
    	List<String> list = getRawItemData(dfn, typeNum, itemNum);
    	
    	List<DiscreteItemData> resultItemData = new ArrayList<DiscreteItemData>();
    	double fmDate = 0;
    	for (String s : list) {
    		fmDate = Double.valueOf(StringUtils.piece(s, 3));
    		if (fmDate >= fmStartDate) {
    			DiscreteItemData data = parseRawItemDataLine(s);
	    		resultItemData.add(data);
    		}
    	}
    	return resultItemData;
    }

    /**
     * Latest data point
     * @param dfn Patient DFN
     * @param typeNum Type number (file number) in which to look
     * @param itemNum Item number (file number) from which to retrieve data
     * @return Latest data record if any exists, or null
     */
    public DiscreteItemData getLatestItemDataPoint(String dfn, String typeNum, String itemNum) {
    	List<String> list = getRawItemData(dfn, typeNum, itemNum);
    	int size = list.size();
    	if (size == 0) {
    		return null;
    	} else {
    		String lastLine = list.get(size-1);
    		DiscreteItemData datapoint = parseRawItemDataLine(lastLine);
    		return datapoint;
    	}
    }

    private DiscreteItemData parseRawItemDataLine(String line) {
    	
		/* ORWGRPC ITEMDATA's output is not completely understood by the author of this DAO.
		 * RPC output schema is also inconsistent among the files that hold the data.
		 * Example RPC output:
		 * 
		 * 63   ^97^3081103.075249^ ^5.5   ^    ^70^BLOOD^^4.2!5.9^%
		 * 63   ^97^3100528.063634^ ^7.1   ^H   ^70^BLOOD^^4.2!5.9^%
		 * 120.5^1 ^3090629.133   ^ ^143/77^    ^1[3090629.133~JUN 29, 2009@13:30]2[10140697~BUHPTSHU,KDUITLAA A]3[1~BLOOD PRESSURE]4[3090629.151756~JUN 29, 2009@15:17:56]5[350~3SE]6[1100~HARMON,ELIZABETH]7[143/77~143/77]8[~]9[~]10[~]11[]12[]
		 * 120.5^1 ^3090701.055343^ ^160/88^    ^1[3090701.055343~JUL 01, 2009@05:53:43]2[10140697~BUHPTSHU,KDUITLAA A]3[1~BLOOD PRESSURE]4[3090701.055402~JUL 01, 2009@05:54:02]5[350~3SE]6[66863~CHAMBERS,KIM]7[160/88~160/88]8[~]9[~]10[~]11[]12[]
		 */
		double fmDate = Double.valueOf(StringUtils.piece(line, 3));

		DiscreteItemData data = new DiscreteItemData();
		
		data.setFileNum(StringUtils.piece(line, 1));
		data.setItemNum(StringUtils.piece(line, 2));
		data.setFmDate(fmDate);
		data.setValue(StringUtils.piece(line, 5));
		data.setAlert(StringUtils.piece(line, 6));

		if (data.getFileNum().equals(LAB_TESTS_FILENUM)) {
    		data.setSampleType(StringUtils.piece(line, 8));
    		data.setReferenceRange(StringUtils.piece(line, 9));
		}
		
		return data;
    }
    
    private List<String> getRawItemData(String dfn, String typeNum, String itemNum) {
    	
    	GregorianCalendar now = new GregorianCalendar();
    	double fmEndDate = FMDateUtils.dateTimeToFMDateTime(now);
    	
    	List<String> result = new ArrayList<String>();

		/* Why, oh why, can't we specify a start date in ORWGRPC ITEMDATA?  We don't want to pull
    	 * in the entire data set since the dawn of time... we only need recent values.
    	 */

    	setDefaultContext("OR CPRS GUI CHART");
    	setDefaultRpcName("ORWGRPC ITEMDATA");
    	String typeItem = typeNum + '^' + itemNum;
    	Object[] params = {typeItem, fmEndDate, dfn};

    	try {
	    	result = lCall(params);
    	} catch (Exception e) {
    		result = null;
    	}
    	
    	return result;
    }

}
