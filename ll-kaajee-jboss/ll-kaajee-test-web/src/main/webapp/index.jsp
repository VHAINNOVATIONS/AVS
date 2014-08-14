<%@ page import="gov.va.med.lom.kaajee.jboss.TestInstitution" %>
<%@ page import="gov.va.med.term.access.Institution" %>

<html>
  <head>
  <title>KAAJEE Institution List</title>
  </head>
  <body>
    <h2>KAAJEE Institution List</h2>
    <% 
      Institution[] instArr = TestInstitution.sites();
      for (int i = 0; i < instArr.length; i++) {
    %>
        <p>
          <%= instArr[i].getName() %> (<%= instArr[i].getStationNumber() %>)
      <% if (instArr[i].getStreetAddress().getCity() != null) { %>
        , <%= instArr[i].getStreetAddress().getCity() %>
      <% } %>
      <% if (instArr[i].getStreetAddress().getState() != null) { %>
        , <%= instArr[i].getStreetAddress().getState().getPostalName() %>
      <% } %>      
        </p>
    <%              
      }
    %>
  </body>
</html>
