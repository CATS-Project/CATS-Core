<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<t:wrapper title="Display corpus">
    <jsp:attribute name="header">
        <c:if test="${state.waitingForConnection}">
            <meta http-equiv="refresh" content="2">
        </c:if>
    </jsp:attribute>
    <jsp:body>
        <div class="row" style="padding-top: 1rem;">
            <c:if test="${state.inprogress}">
                <div class="col s6">
                    <a  href="<c:url value="/corpus/${id}/machin.csv"/>" download class="waves-effect waves-light btn">
                        <i class="material-icons right">import_export</i>Download this corpus in CSV</a>

                    <a onclick="$.ajax({method:'delete',url:'<c:url value="/corpus/${id}/collect" />'}).success(function(){location.reload();})" class="waves-effect waves-light btn red">
                        <i class="material-icons right">stop</i>Stop the collect</a>
                </div>

            </c:if>
            <c:if test="${state.shutdown}">
                <div class="col s12">
                    <a  href="<c:url value="/corpus/${id}/machin.csv"/>" download class="waves-effect waves-light btn">
                        <i class="material-icons right">import_export</i>Download this corpus in CSV</a>
                </div>
            </c:if>
            <c:if test="${state.error}">
                <div class="col s6">
                    <a  href="<c:url value="/corpus/${id}/machin.csv"/>" download class="waves-effect waves-light btn">
                        <i class="material-icons right">import_export</i>Download this corpus in CSV</a>
                </div>
                <div class="col s6">
                    This corpus had finished with an error. Reason : ${errorMsg}
                </div>
            </c:if>
            <c:if test="${state.waitingForConnection}">
                <div>The connection is establishing, please wait...</div>
            </c:if>
        </div>

        <%--<div class="row">
            <form action="<c:url value="/corpus/search"/>" method="post">
                <div class="input-field col s6">
                  <input value="" id="search" type="text" class="validate">
                  <label class="active" for="search">Search</label>
                </div>

                <button style="margin-top:25px;" class="btn waves-effect waves-light" type="submit" name="action">SEARCH</button>
            </form>
        </div>--%>



        <div class="row">
            <table>
                <thead>
                    <tr>
                        <th data-field="name">Name</th>
                        <th data-field="text">Text</th>
                        <th data-field="descriptionauthor">Description Author</th>
                        <th data-field="date">Date</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="entry" items="${list}" >
                    <tr>
                        <td>${entry.name}</td>
                        <td>${entry.text}</td>
                        <td>${entry.descriptionAuthor}</td>
                        <td><fmt:formatDate value="${entry.date}" pattern="yyyy-MM-dd HH:mm" /></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <br>
            <a style="display:inline;" href="<c:url value="/corpus/${id}/1"/>">FirstPage</a>
            <ul class="pagination" style="display:inline;">
                <li class="${currentPage-1<1?"disabled waves-effect":"waves-effect"}"><a <c:if test="${currentPage-1 ge 1}">href="<c:url value="/corpus/${id}/${currentPage-1}"/>"></c:if><i class="material-icons">chevron_left</i></a></li>
                <c:forEach var="i" begin="1" end="${nbrPage}" step="1">
                    <c:if test="${currentPage < i+5 && currentPage > i-5}" >
                        <li <c:if test="${i eq currentPage}" >class="active"</c:if><c:if test="${i ne currentPage}" >class="waves-effect"</c:if>>
                            <a href="<c:url value="/corpus/${id}/${ i }"/>">
                                <c:out value="${ i }" />
                            </a>
                        </li>
                    </c:if>
                </c:forEach>
                <li class="${currentPage+1>nbrPage?"disabled waves-effect":"waves-effect"}"><a <c:if test="${currentPage+1<=nbrPage}">href="<c:url value="/corpus/${id}/${currentPage+1}"/>"></c:if><i class="material-icons">chevron_right</i></a></li>
            </ul>
            <a style="display:inline;" href="<c:url value="/corpus/${id}/${nbrPage}"/>">LastPage</a>
        </div>
    </jsp:body>
</t:wrapper>