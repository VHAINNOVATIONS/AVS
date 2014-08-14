package gov.va.med.lom.avs.client.thread;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.LinkedHashMap;

import gov.va.med.lom.foundation.service.response.CollectionServiceResponse;
import gov.va.med.lom.foundation.service.response.ServiceResponse;

import gov.va.med.lom.javaUtils.misc.DateUtils;
import gov.va.med.lom.javaUtils.misc.StringUtils;

import gov.va.med.lom.vistabroker.util.FMDateUtils;

import gov.va.med.lom.avs.client.model.OrderJson;
import gov.va.med.lom.avs.web.util.AvsWebUtils;
import gov.va.med.lom.avs.enumeration.DGroupSeq;
import gov.va.med.lom.avs.enumeration.TranslationTypeEnum;

public class OrdersThread extends SheetDataThread {

  public void run() {
    
    StringBuffer body = new StringBuffer();
    List<OrderJson> ordersList = new ArrayList<OrderJson>();
    try {
      ServiceResponse<LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>>> response = super.getSheetService()
          .getTodaysOrders(super.securityContext, super.getEncounterInfo());
        LinkedHashMap<DGroupSeq, SortedMap<String,List<String>>> orders = response.getPayload();

      if (orders.size() == 0) {
        body.append(super.getStringResource("none"));
  
      } else {
        
        Calendar today = Calendar.getInstance();
        
        int dGroupIndex = 0;
        Iterator<DGroupSeq> it = orders.keySet().iterator();
        while (it.hasNext()) {
          DGroupSeq dGroup = it.next();
          if (dGroupIndex > 0) {
            body.append("<br/>");
          }
          dGroupIndex++;
          body.append("<div class=\"orders-category\">").append(dGroup.getName()).append("</div><br/>\n");
          if (dGroup.getName().equals(DGroupSeq.MED.getName())) {
            String str = super.getStringResource("medOrdersInstructions");
            if (!str.isEmpty()) {
              body.append("<div class=\"orders-instructions\">")
                .append(str)
                .append("</div><br/>\n");
            }
            
          } else if (dGroup.getName().equals(DGroupSeq.CONSULT.getName())) {
            String str = super.getStringResource("consultOrdersInstructions");
            if (!str.isEmpty()) {
              body.append("<div class=\"orders-instructions\">")
                .append(str)
                .append("</div><br/>\n");
            }
            
          } else if (dGroup.getName().equals(DGroupSeq.LAB.getName())) {
            String str = super.getStringResource("labOrdersInstructions");
            if (!str.isEmpty()) {
              body.append("<div class=\"orders-instructions\">")
                .append(str)
                .append("</div><br/>\n");
            }
          } else if (dGroup.getName().equals(DGroupSeq.IMAGING.getName())) {
            String str = super.getStringResource("imagingOrdersInstructions");
            if (!str.isEmpty()) {
              body.append("<div class=\"orders-instructions\">")
                .append(str)
                .append("</div><br/>\n");
            }
          }
          String category = null;
          SortedMap<String,List<String>> descriptions = orders.get(dGroup);
          int categoryIndex = 0;
          for (String sortKey : descriptions.keySet()) {
            String date = null;
            Calendar dt = Calendar.getInstance();
            try {
              dt.setTime(FMDateUtils.fmDateTimeToDate(Double.valueOf(sortKey)));
              date = DateUtils.toDateTimeStr(dt.getTime(), "MMMM dd, yyyy");
            } catch (Exception e) {};
            if (dGroup.getName().equals(DGroupSeq.MED.getName())) {
              category = StringUtils.mixedCase(sortKey);
            } else {
              if ((sortKey != null) && (!sortKey.isEmpty())) {
                if (today.get(Calendar.DAY_OF_YEAR) == dt.get(Calendar.DAY_OF_YEAR)) {
                  category = super.getStringResource("today");
                } else {
                  category = date;
                }
              }
            }
            if ((category != null) && (!category.isEmpty())) {
              if (categoryIndex > 0) {
                body.append("<br/>");
              }
              categoryIndex++;
              body.append("<div class=\"orders-subitem\">")
              .append(category)
              .append("</div><br/>\n");
            }
            
            List<String> sortedOrders = descriptions.get(sortKey);
            Collection<String> translated = null;
            try {
              CollectionServiceResponse<String> csr = super.getSettingsService().translateStrings(
                  super.stationNo, super.getLanguage(), TranslationTypeEnum.ORDER_TEXT, sortedOrders);
              AvsWebUtils.handleServiceErrors(csr, log);
              translated = csr.getCollection();
            } catch(Exception e) {
              e.printStackTrace();
              translated = new ArrayList<String>();
            }
            body.append("<div class=\"orders-list\">");
            body.append("<ul>");
            for (String desc : translated) {
              desc = StringUtils.mixedCase(desc);
              OrderJson orderJson = new OrderJson();
              orderJson.setType(dGroup.getName());
              if (date != null) {
                orderJson.setDate(date);
              } else {
                try {
                  orderJson.setDate(DateUtils.toDateTimeStr(DateUtils.fmDateTimeToDateTime(
                      super.getLatestEncounter().getEncounterDatetime()).getTime(), "MMMM dd, yyyy"));
                } catch(Exception e) {}
              }
              orderJson.setText(desc.trim());
              ordersList.add(orderJson);
              body.append("<li>");
              body.append(desc);
              body.append("</li>\n");
            }
            body.append("</ul>");
            body.append("</div>");
          }
        }
      }
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")
        .replace("__SECTION_ID_SUFFIX__", "orders")
        .replace("__SECTION_TITLE__", super.getStringResource("orders"))
        .replace("__CONTENTS__", body.toString());
      
      setContentData("orders", content, ordersList);
    }
    
  }
  
}
