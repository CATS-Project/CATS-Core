<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:wrapper title="Corpus : ${corpus.name}">
	<jsp:attribute name="header">
        <script src="<c:url value="/resources/js/angular.min.js"/>"></script>
        <script>
            var myApp = angular.module('modules-chooser', []);
        </script>
        
    </jsp:attribute>
    <jsp:body>
        <div class="row" style="padding-top: 1rem;">
            <c:if test="${corpus.state.inprogress}">
                <div class="col s6">
                    <a href="<c:url value="/corpus/${corpus.id}/machin.csv"/>" download
                       class="waves-effect waves-light btn">
                        <i class="material-icons right">import_export</i>Download my corpus in CSV</a>
                    <a onclick="$.ajax({method:'delete',url:'<c:url
                            value="/corpus/${corpus.id}/collect"/>'}).success(function(){location.reload();})"
                       class="waves-effect waves-light btn red">
                        <i class="material-icons right">stop</i>Stop the collect</a>
                </div>

            </c:if>
            <c:if test="${corpus.state.shutdown}">
                <div class="col s12">
                    <a href="<c:url value="/corpus/${corpus.id}/machin.csv"/>" download
                       class="waves-effect waves-light btn">
                        <i class="material-icons right">import_export</i>Download my corpus in CSV</a>
                </div>
                <div class="col s6">
                    <a onclick="$.ajax({method:'delete',url:'<c:url
                            value="/corpus/${corpus.id}"/>'}).success(function(){location.replace('<c:url
                            value="/corpus"/>');})"
                       class="waves-effect waves-light btn red">
                        <i class="material-icons right">delete</i>Delete this corpus</a>
                </div>
            </c:if>
            <c:if test="${corpus.state.error}">
                <div class="col s6">
                    <a href="<c:url value="/corpus/${corpus.id}/machin.csv"/>" download
                       class="waves-effect waves-light btn">
                        <i class="material-icons right">import_export</i>Download my corpus in CSV</a>
                </div>
                <div class="col s6">
                    This corpus had finished with an error. Reason : ${corpus.errorMessage}
                </div>
                <div class="col s6">
                    <a onclick="$.ajax({method:'delete',url:'<c:url
                            value="/corpus/${corpus.id}"/>'}).success(function(){location.replace('<c:url
                            value="/corpus"/>');})"
                       class="waves-effect waves-light btn red">
                        <i class="material-icons right">delete</i>Delete this corpus</a>
                </div>
            </c:if>
            <c:if test="${corpus.state.waitingForConnection}">
                <div>The connection is establishing, please wait...</div>
            </c:if>
            <div class="col 6">
                <a class="waves-effect waves-light btn blue" href="<c:url value="/corpus/${corpus.id}/1"/>">
                    <i class="material-icons right">toc</i>See the corpus content</a>
            </div>
        </div>

        <div class="row">

        </div>

        <div class="row">
            <div class="col s12">
                <div class="card-panel">
                    <table class="bordered">
                        <c:if test="${corpus.calculated}">
                        <tr>
                            <td>Calculated from :</td>
                            <td>${corpus.father.name}</td>
                        </tr>
                        </c:if>


                        <tr>
                            <td>Total :</td>
                            <td>${corpus.count}</td>
                        </tr>
                        <c:if test="${!corpus.calculated}">
                            <tr>
                                <td>Lauched :</td>
                                <td>${corpus.launchDateString}</td>
                            </tr>
                            <c:if test="${corpus.stopDate != null}">
                                <tr>
                                    <td>Stopped :</td>
                                    <td>${corpus.stopDateString}</td>
                                </tr>
                            </c:if>
                            <tr>
                                <td>Duration :</td>
                                <td>${corpus.duree}</td>
                            </tr>

                            <tr>
                                <td>State :</td>
                                <td><c:if test="${corpus.state.error}">Error : ${corpus.errorMessage}</c:if>
                                    <c:if test="${corpus.state.waitingForConnection}">Waiting</c:if>
                                    <c:if test="${corpus.state.shutdown}">Finished</c:if>
                                    <c:if test="${corpus.state.inprogress}">Currently Running</c:if></td>
                            </tr>
                            <tr>
                                <td>Criteria:</td>
                                <td></td>
                            </tr>


                            <c:if test="${corpus.follows!=null}">
                                <tr>
                                    <td>Users:</td>
                                    <td><c:forEach items="${corpus.follows}" var="user">
                                        ${user}
                                    </c:forEach></td>
                                </tr>
                            </c:if>
                            <c:if test="${corpus.keyWords!=null}">
                                <tr>
                                    <td>Keywords:</td>
                                    <td><c:forEach items="${corpus.keyWords}" var="keywords">
                                        ${keywords}
                                    </c:forEach></td>
                                </tr>
                            </c:if>
                            <c:if test="${corpus.location != null}">
                                <tr>
                                    <td>Location:</td>
                                    <td>North-East (${corpus.location.neLat},${corpus.location.neLng}),
                                        South-West (${corpus.location.swLat},${corpus.location.swLng})
                                    </td>
                                </tr>
                            </c:if>
                        </c:if>
                        <c:if test="${corpus.calculated}">
                            <tr>
                                <td>Calculated :</td>
                                <td>${corpus.launchDateString}</td>
                            </tr>
                        </c:if>
                    </table>

                </div>
            </div>
        </div>

        <div class="row" style="padding-top: 1rem;">
            <div class="col s12">
                <c:forEach items="${corpus.requests}" var="request">
                    <div class="row">
                        <div class="col s12">
                            <div class="card">
                                <div class="card-content">
                                    <span class="card-title black-text">${request.module.name}</span>

                                    <p>Lauched At ${request.initDateFormat}</p>
                                    <c:forEach items="${request.params}" var="entry">
                                        <p>${entry.key}: ${entry.value}</p>
                                    </c:forEach>

                                    <c:if test="${request.finished}">
                                        <c:forEach items="${request.results}" var="result">
                                            <p>At ${result.date}, the module returns
                                                <c:if test="${result.type.HTML}">
                                                    a visualisable result
                                                </c:if>
                                                <c:if test="${result.type.file}">
                                                    a downloadable file
                                                </c:if>
                                                <c:if test="${result.type.corpus}">
                                                    a new corpus
                                                </c:if>
                                                <c:if test="${result.type.error}">
                                                    an error : Reason : ${result.result}
                                                </c:if>
                                            </p>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${!request.finished}">
                                        <p>Running…</p>
                                    </c:if>


                                </div>
                                <div class="card-action">
                                    <c:if test="${request.finished}">
                                        <a href="javascript:;" onclick="$.ajax({method:'delete',url:'<c:url
                                                value="/module/request/${request.id}"/>'}).success(function(){location.reload();})"
                                           class="red-text">Delete this result</a>
                                    </c:if>
                                    <c:forEach items="${request.results}" var="result">
                                        <c:if test="${result.type.HTML}">
                                            <a href="<c:url value="/module/request/${request.id}" />">Explore the
                                                result</a>
                                        </c:if>
                                        <c:if test="${result.type.file}">
                                            <a href="<c:url value="/module/requestFile/${request.id}" />" download>Download
                                                the file</a>
                                        </c:if>
                                        <c:if test="${result.type.corpus}">
                                            <a href="<c:url value="/corpus/${result.result}" />">See the new corpus</a>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <c:if test="${fn:length(corpus.requests) eq 0}">
                You don't have any treatments on this corpus.
            </c:if>
        </div>
    </jsp:body>
</t:wrapper>
