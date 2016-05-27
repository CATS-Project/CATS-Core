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
        <form class="col s12" name='loginForm' action="<c:url value="/j_spring_security_check"></c:url>" method='POST'>
	        <div class="row">
	        	<c:if test="${not empty error}">
					${error}
				</c:if>
				<c:if test="${not empty msg}">
					${msg}
				</c:if>
			</div>
            <div class="row">
                <div class="input-field col s12 l6 m6">
                    <input id="login" name="login" type="text" class="validate" style="text-transform: lowercase;" value="">
                    <label for="login">Login</label>
                </div>
                <div class="input-field col s12 l6 m6">
                    <input id="password" name="password" type="password" class="validate" value="">
                    <label for="password">Password</label>
                </div>
            </div>
             <div class="row">
                <div class="col s12 l6 m6">
                    <button class="btn btn-little waves-effect waves-light" style="width:18em;" type="submit">
		                Login
		            </button>
                </div>
                <div class="col s12 l6 m6">
                   <a class="right-align btn waves-effect waves-light" style="width:18em;" href="<c:url value="/register"></c:url>">
		                REGISTER
		            </a>
                </div>
            </div>
            
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
        <h4>About CATS</h4>
		<p>Twitter presents an unparalleled opportunity for researchers from various fields to gather valuable and genuine textual data from millions of people. However, the collection process, as well as the analysis of these data require different kinds of skills (e.g. programing, data mining) which can be an obstacle for people who do not have this background.</p>
		<p>CATS (i.e. Collection and Analysis of Tweets made Simple) is an open-source, scalable, Web-application designed to support researchers who want to carry out studies based on tweets. The purpose of CATS is twofold: (i) allow people to collect tweets (ii) enable them to analyze these tweets thanks to efficient tools (e.g. event detection, topic modeling, named-entity recognition).</p>
        <p>To learn more about CATS, check out:
            <ul>
                <li>The wiki: <a href="https://github.com/CATS-Project/CATS-Core/wiki">https://github.com/CATS-Project/CATS-Core/wiki</a></li>
                <li>The paper: <a href="http://dl.acm.org/citation.cfm?id=2874320">http://dl.acm.org/citation.cfm?id=2874320</a></li>
                <li>The source code: <a href="https://github.com/CATS-Project/">https://github.com/CATS-Project/</a></li>
            </ul>
        </p>

        <h4>Contributors</h4>
        <p>
        Current contributors:
            <ul>
                <li><a href="mailto:adrien.guille@univ-lyon2.fr">Adrien Guille</a>, PhD in Computer Science, University of Lyon (ERIC, Lyon 2)</li>
                <li><a href="mailto:Michael.Gauthier@univ-lyon2.fr">Michael Gauthier</a>, PhD student in Socio-Linguistics, University of Lyon (CRTT, Lyon 2)</li>
                <li><a href="mailto:anthony.deseille@etu.univ-lyon1.fr">Anthony Deseille</a>, Graduate student in Computer Science, University of Lyon (ERIC, Lyon 1)</li>
            </ul>
        Past contributors:
            <ul>
                <li><a href="https://cs.pub.ro/index.php/people/userprofile/ciprian_truica">Ciprian-Octavian Truica</a>, PhD student in Computer Science, University Politehnica of Bucharest</li>
            </ul>
        </p>
    </div>
</div>
</body>
</html>
