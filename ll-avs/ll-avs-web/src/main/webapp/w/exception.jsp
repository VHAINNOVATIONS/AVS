<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="application/json; charset=UTF-8" %>
<% response.setStatus(500); %>
{"success": false,"errors": "<s:property value="%{exception.message}"/>"}
