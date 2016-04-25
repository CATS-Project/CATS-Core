<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:wrapper title="Manage modules">
	<jsp:attribute name="header">
        <script>
            $(document).ready(function () {
                $('select').material_select();
            });
        </script>
        <script src="<c:url value="/resources/js/angular.min.js"/>"></script>
        <script src="<c:url value="/resources/js/create-module.js"/>"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="row" data-ng-app="module-form">
            <div class="col s12">
                <div class="card">
                    <div class="card-content">
                        <span class="card-title black-text">Modules List</span>

                        <div class="row">
                            <div class="input-field col s12">
                                <table class="bordered highlight responsive-table">
                                    <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Endpoint</th>
                                        <th>Parameters</th>
                                        <th>Results</th>
                                        <th>Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${modules}" var="module">
                                        <tr>
                                            <td>${module.name}</td>
                                            <td>${module.description}</td>
                                            <td>${module.endpoint}</td>
                                            <td>
                                                <ul>
                                                    <c:forEach items="${module.params}" var="parameter">
                                                        <li>${parameter.displayName} : ${parameter.type}</li>
                                                    </c:forEach>
                                                </ul>
                                            </td>

                                            <td>
                                                <ul>
                                                    <c:forEach items="${module.returns}" var="returned">
                                                        <li>${returned}</li>
                                                    </c:forEach>
                                                </ul>
                                            </td>

                                            <td><a class="waves-effect waves-light btn-floating red" href="javascript:;"
                                                   onclick="$.ajax({method:'delete',url:'<c:url
                                                           value="/admin/modules/${module.id}"/>'}).success(function(){location.reload();})">
                                                <i class="material-icons left">remove</i></a></td>

                                        </tr>
                                    </c:forEach>
                                    </tbody>

                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card">
                    <div class="card-content">
                        <span class="card-title black-text">Add a new module</span>

                        <div class="row">
                            <form class="col s12" method="post" action="<c:url value="/admin/modules"/>">

                                <div class="row">
                                    <div class="input-field col s6">
                                        <input id="name" type="text" class="validate" name="name">
                                        <label for="name">Name</label>
                                    </div>
                                    <div class="input-field col s6">
                                        <input id="endpoint" type="text" class="validate" name="endpoint">
                                        <label for="endpoint">Endpoint URL</label>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="input-field col s12">
                                <textarea id="description" name="description" class="materialize-textarea"
                                          placeholder="Explain what your module does (HTML supported)"></textarea>
                                        <label for="description">Description</label>
                                    </div>
                                </div>
                                <div class="row" data-ng-controller="parameter">
                                    <div class="col s6">
                                        <h5>Parameters:</h5>

                                        <div class="row">
                                            <div class="input-field col s3">
                                                <input id="nameParamInput" type="text" class="validate"
                                                       data-ng-model="current.name">
                                                <label for="nameParamInput">Name</label>
                                            </div>
                                            <div class="input-field col s4">
                                                <input id="displayNameParamInput" type="text" class="validate"
                                                       data-ng-model="current.displayName">
                                                <label for="displayNameParamInput">Display Name</label>
                                            </div>
                                            <div class="input-field col s4">
                                                <select data-ng-model="current.type">
                                                    <option value="" disabled selected>Choose a type</option>
                                                    <option value="text">Text</option>
                                                    <option value="number">Number</option>
                                                    <option value="bool">Boolean</option>
                                                </select>
                                                <label>Type</label>
                                            </div>
                                            <div class="col s1">
                                                <a class="waves-effect waves-light btn-floating"
                                                   data-ng-click="add()"><i
                                                        class="material-icons left">add</i></a>
                                            </div>
                                        </div>

                                        <div class="row" data-ng-repeat="param in list track by $index">
                                            <div class="input-field col s3">
                                                {{param.name}}
                                                <input type="hidden" name="nameParameter" value="{{param.name}}">
                                            </div>
                                            <div class="input-field col s4">
                                                {{param.displayName}}
                                                <input type="hidden" name="displayNameParameter"
                                                       value="{{param.displayName}}">
                                            </div>
                                            <div class="input-field col s4">
                                                {{param.type}}
                                                <input type="hidden" name="typeParameter" value="{{param.type}}">
                                            </div>
                                            <div class="col s1">
                                                <a class="waves-effect waves-light btn-floating red"
                                                   data-ng-click="remove($index)">
                                                    <i class="material-icons left">remove</i></a>
                                            </div>
                                        </div>


                                    </div>
                                    <div class="input-field col s6">
                                        <h5>Returns type:</h5>

                                        <p>
                                            <input type="checkbox" id="html" value="html" name="returns"/>
                                            <label for="html">A visualisable result in HTML</label>
                                        </p>

                                        <p>
                                            <input type="checkbox" id="data" value="data" name="returns"/>
                                            <label for="data">A downloadable file in the format you want</label>
                                        </p>

                                        <p>
                                            <input type="checkbox" id="corpus" value="corpus" name="returns"/>
                                            <label for="corpus">A new corpus will be added</label>
                                        </p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col s12">
                                        <button class="btn waves-effect waves-light" type="submit">Submit
                                            <i class="material-icons right">send</i>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:wrapper>

