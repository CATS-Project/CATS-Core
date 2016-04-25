<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:wrapper title="Sub-Corpus Details">
	<jsp:attribute name="header">
       
    </jsp:attribute>
    <jsp:body>
        <div class="row" style="padding-top: 1rem;">
            <div class="col s12">
                <a  href="<c:url value="/sub/${sub.id}/sub.csv"/>" download class="waves-effect waves-light btn">
                    <i class="material-icons right">import_export</i>Dowload my sub corpus in CSV</a>
            </div>
            <div class="col s12">
                <a class="waves-effect waves-light btn blue" href="<c:url value="/sub/${sub.id}/1"/>">
                    <i class="material-icons right">toc</i>See the sub-corpus content</a>
            </div>
        </div>

        <div class="row" style="padding-top: 1rem;">
        	<div class="input-field col s6">
                <label>Sub corpus Name : ${sub.name}</label>
            </div>
            <div class="input-field col s6">
                <label>Regular Expression : ${sub.regex}</label>
            </div>
        </div>
        
        <div class="row" style="padding-top: 1rem;">
        	<div class="input-field col s6">
                <label>Hashtags : 
               		<c:forEach var="entry" items="${sub.hashtags}" >
                        ${entry}, 
                    </c:forEach>
                 </label>
            </div>
            <div class="input-field col s6">
                <label>Mentions : 
                	<c:forEach var="entry" items="${sub.mentions}" >
                        ${entry}, 
                    </c:forEach>
                </label>
            </div>
        </div>
        <div class="row" style="padding-top: 1rem;">
        	<div class="input-field col s6">
                <label>Date de création : ${sub.creationDate}
                 </label>
            </div>
            <div class="input-field col s6">
                <label>Total : ${sub.count}
                </label>
            </div>
        </div>

        <div class="row" style="padding-top: 1rem;">
            <div class="col s12">
                <c:forEach items="${sub.requests}" var="request">
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

                <c:if test="${fn:length(sub.requests) eq 0}">
                    <p>You don't have any treatments on this corpus.</p>
                </c:if>
            </div>


        </div>
    </jsp:body>
</t:wrapper>
