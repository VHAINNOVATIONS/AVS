
<html>

<form action="<%=request.getContextPath()%>/login/logincontroller" method="POST">

Site: <input type="text" name="STATION"/><br/>


Access Code: <input type="password" name="ACCESS"/> <br/>


Verify Code: <input type="password" name="VERIFY"/> <br/>



<input type="submit"/>

</form>




</html>