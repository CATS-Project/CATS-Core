<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="title" required="true" fragment="false" %>
<%@attribute name="header" fragment="true" required="false" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>CATS : ${title}</title>
    <jsp:include page="../pages/head.jsp" />
    <jsp:invoke fragment="header"/>
    <style>
        ul.side-nav.fixed li.no-hover {
            background-color: transparent;
        }

        ul.side-nav.fixed li {
            width: 100%;
        }
    </style>
</head>
<body>
<header>
    <nav class="top-nav">
        <div class="container">
            <div class="nav-wrapper"><a class="page-title">${title}</a></div>
        </div>
    </nav>
    <ul id="nav-mobile" class="side-nav fixed" style="width: 240px;">
        <li class="center no-hover"><img src="${user.imageUrl}"></li>
        <li class="center no-hover"><h4>${user.twitterName}</h4></li>
        <li class="waves-effect"><a href="<c:url value="/corpus"/>">My corpora (${user.corpus.size()})</a></li>
        <li class="waves-effect"><a href="<c:url value="/corpus/create"/>">Create a new corpus</a></li>
        <li class="waves-effect"><a href="<c:url value="/sub"/>">Create a sub-corpus</a></li>
        <sec:authorize access="hasAuthority('ROLE_ADMIN')">
            <li class="waves-effect"><a href="<c:url value="/admin/accounts"/>">Manage user accounts</a></li>
            <li class="waves-effect"><a href="<c:url value="/admin/modules"/>">Manage modules</a></li>
        </sec:authorize>
        <c:if test="${!user.hasAllTheirTokens()}">
            <li class="waves-effect"><a href="<c:url value="/associate"/>">Associate my twitter account</a></li>
        </c:if>
        <li class="waves-effect waves-blue" style="background-color: #55acee;"><a href="<c:url value="/logout"/>">Disconnect</a></li>

    </ul>
</header>
<main>
    <div class="container" style="padding-top: 1rem;">
        <jsp:doBody />
    </div>
</main>
</body>
</html>