<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Nathanael
  Date: 19/10/2015
  Time: 08:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Welcome !</title>
    <jsp:include page="head.jsp"/>
</head>
<body>
<jsp:include page="../pages/navbar.jsp" />
<div class="container">
    <div class="row">
        <form class="col s12" name='registerForm' action="<c:url value="/newUser"></c:url>" method='POST' >
	        <div class="row">
	        	<c:if test="${not empty error}">
					${error}
				</c:if>
			</div>
            <div class="row">
                <div class="input-field col s6">
                    <input id="login" name="login" type="text" style="text-transform: lowercase;" class="validate">
                    <label for="login">Login *</label>
                </div>
                <div class="input-field col s6">
                    <input id="email" name="email" type="text" class="validate">
                    <label for="email">Email *</label>
                </div>
             </div>
             <div class="row">
                <div class="input-field col s6">
                    <input id="password" name="password" type="password" class="validate">
                    <label for="password">Password *</label>
                </div>
                 <div class="input-field col s6">
                    <input id="passwordc" name="passwordc" type="password" class="validate">
                    <label for="passwordc">Confirm Password *</label>
                </div>
           	 </div>
           	 <div class="row">
                <div class="input-field col s6">
                    <input id="firstName" name="firstName" type="text" class="validate">
                    <label for="firstName">First Name</label>
                </div>
                <div class="input-field col s6">
                    <input id="lastName" name="lastName" type="text" class="validate">
                    <label for="lastName">Last Name</label>
                </div>
            </div>
            <button class="btn waves-effect waves-light" type="submit" style="width:18em;">
                Register
            </button>
        </form>
    </div>
</div>
</body>
</html>
