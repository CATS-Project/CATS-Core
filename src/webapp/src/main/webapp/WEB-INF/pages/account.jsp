<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<t:wrapper title="Users administration">
    <jsp:attribute name="header">
     	<script src="<c:url value="/resources/js/delete_modal.js"/>"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="row" data-ng-app="corpus-form">
            <div class="col s12">
                <div class="row">
                    <div class="input-field col s12">
                        <h5>User's List</h5>
                        
                         <table class="bordered highlight responsive-table">
					        <thead>
					          <tr>
					              <th data-field="login">Login</th>
					              <th data-field="fname">First Name</th>
					              <th data-field="lname">Last Name</th>
					              <th data-field="tname">Twitter Name</th>
					              <th data-field="email">Email</th>
					              <th data-field="activated">Activated</th>
					              <th >Actions</th>
					          </tr>
					        </thead>
					        <tbody>
						        <c:forEach var="userItem" items="${users.content}">
						         <c:if test="${userItem.login ne user.login}">
						         <tr>
						            <td><c:out value="${userItem.login}"/><br></td>
						            <td><c:out value="${userItem.firstName}"/><br></td>
						            <td><c:out value="${userItem.lastName}"/><br></td>
						            <td><c:out value="${userItem.twitterName}"/><br></td>
						            <td><c:out value="${userItem.email}"/><br></td>
						            <td><c:out value="${userItem.activated}"/><br></td>
						            <td>
										<c:choose>
										    <c:when test="${! userItem.activated}">
										  	  <a title="activate User" href="<c:url value="/admin/activateUser?idu=${userItem.id}"/>">
												<i class="small material-icons">lock_open</i>
											  </a> 
										    </c:when>
										    <c:otherwise>
										      <a title="desactivate User" href="<c:url value="/admin/desactivateUser?idu=${userItem.id}"/>">
												<i class="small material-icons">lock</i>
											  </a> 
										    </c:otherwise>
										</c:choose> 
										<a title="delete User" class="del_button" id="${userItem.id}" style="cursor: pointer;" >
												<i class="small material-icons">delete</i>
										</a>
									</td>
						         </tr>
						         </c:if>
								</c:forEach>
					        </tbody>
					      </table>
					       <ul class="pagination">
				                <li <c:if test="${!users.first}" >class="waves-effect"</c:if><c:if test="${users.first}" >class="disabled"</c:if>><a href="<c:url value="/admin/accounts/${users.number - 1}"/>"><i class="material-icons">chevron_left</i></a></li>
				                <c:forEach var="i" begin="0" end="${users.totalPages-1}" step="1">
				                    <li <c:if test="${i == users.number}" >class="active"</c:if><c:if test="${i != users.number}" >class="waves-effect"</c:if>>
				                        <a href="<c:url value="/admin/accounts/${ i }"/>">
				                            <c:out value="${ i }" />
				                        </a>
				                    </li>
				                </c:forEach>
				                <li <c:if test="${!users.last}" >class="waves-effect"</c:if><c:if test="${users.last}" >class="disabled"</c:if>><a href="<c:url value="/admin/accounts/${users.number + 1}"/>"><i class="material-icons">chevron_right</i></a></li>
				            </ul>
                    </div>
                </div>
            </div>
        </div>
        <div id="modal1" class="modal">
	    	<div class="modal-content">
		      <h4>Confirm User delete</h4>
		      <p>Are you sure you want to delete this user ?</p>
		    </div>
		    <div class="modal-footer">
		      <a href="#!" class="accept_delete modal-action modal-close waves-effect waves-red btn-flat">Yes</a>
		      <a href="#!" class="cancel_delete modal-action modal-close waves-effect waves-green btn-flat">No</a>
		    </div>
		</div>

    </jsp:body>
</t:wrapper>