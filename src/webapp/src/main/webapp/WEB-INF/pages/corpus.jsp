<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<t:wrapper title="Dashboard">
	<jsp:attribute name="header">
     	<script type="text/javascript">
            function initModules() {
                var modules = $('#selectedModules > li');
                var idAttr;
                var modulesOrder = "";
                for (var i = 0; i < modules.length; i++) {
                    idAttr = modules[i].id;
                    if (typeof idAttr !== typeof undefined && !idAttr == "") {
                        modulesOrder += modules[i].id;
                        if (i != modules.length - 1) {
                            modulesOrder += ",";
                        }
                    }
                }
                $('#modulesIDS').attr("value", modulesOrder);
            }
            $(document).ready(function () {
                $('select').material_select();
                $(".chainModule").click(
                        function (e) {

                            var $ul = $(this).closest('ul');
                            var $ulId = $ul[0].id;
                            if ($ulId == "myModules") {
                                $('#selectedModules').append($(this).closest('li')[0]);
                                $(this).html($(this).html().replace("keyboard_arrow_right", "keyboard_arrow_left"));
                            }
                            else if ($ulId == "selectedModules") {
                                $('#myModules').append($(this).closest('li')[0]);
                                $(this).html($(this).html().replace("keyboard_arrow_left", "keyboard_arrow_right"));
                            }

                            e.preventDefault();
                        });

                $("form select[name=corpusId]").on("change", function(){
                    if($('option:selected', this).attr("class") == "subcorpus")
                        $($(this).parents("form")[0]).children("input[name=subcorpus]").val("true");
                    else
                        $($(this).parents("form")[0]).children("input[name=subcorpus]").val("false");

                });

            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <div class="row">
            <div class="col s12">
                <ul class="collection with-header">
                    <li class="collection-header"><h4>My corpora</h4></li>
                    <c:forEach var="entry" items="${corpus}">
                        <a href="<c:url value="/corpus/${entry.id}"/>"
                           class="collection-item">${entry.name}
                            <c:if test="${entry.state.error}">
                                <span class="badge new red">${entry.count}</span>
                            </c:if>
                            <c:if test="${entry.state.inprogress}">
                                <span class="badge new green">${entry.count}</span>
                            </c:if>
                            <c:if test="${entry.state.shutdown}">
                                <span class="badge">${entry.count}</span>
                            </c:if>
                            <c:if test="${entry.state.waitingForConnection}">
                                <span class="badge new blue">${entry.count}</span>
                            </c:if>
                        </a>
                        <c:forEach var="sub" items="${entry.subCorpuses}">
                            <a href="<c:url value="/sub/${sub.id}"/>" class="collection-item subCorpuses">
                                <i class="tiny material-icons">navigation</i> ${sub.name} <span
                                    class="badge">${sub.count}</span>
                            </a>
                        </c:forEach>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <div class="row" data-ng-app="modules-chooser">
            <div class="col s12">
                <h4>Here are the actions available:</h4>
                <c:if test="${fn:length(modules) ne 0}">
                    <ul class="collapsible popout" data-collapsible="accordion">
                        <c:forEach items="${modules}" var="module">
                            <li>
                                <div class="collapsible-header">
                                    <i class="material-icons">settings</i>${module.name}</div>
                                <div class="collapsible-body"
                                     style="padding: 2rem;">
                                    <p>${module.description}</p>

                                    <form method="post"
                                          action="<c:url value="/module/request/${module.id}"/>">
                                        <div class="input-field s12">
                                            <select name="corpusId" required>
                                                <option value=""  disabled selected>Choose your option</option>
                                                <c:forEach var="corpu" items="${corpus}">
                                                    <option value="${corpu.id}">${corpu.name}</option>
                                                    <c:forEach var="subcorpus" items="${corpu.subCorpuses}">
                                                        <option value="${subcorpus.id}" class="subcorpus">&nbsp;&nbsp;&nbsp;${subcorpus.name}</option>
                                                    </c:forEach>
                                                </c:forEach>
                                            </select>
                                            <label>Corpus</label>
                                        </div>

                                        <c:forEach items="${module.params}"
                                                   var="paramModule">

                                            <div class="row">
                                                <div
                                                        class="input-field col s12">
                                                    <input
                                                            id="input${paramModule.id}" name="${paramModule.name}"
                                                            type="${paramModule.typeHTML}" step="0.1"/>
                                                    <label
                                                            for="input${paramModule.id}">${paramModule.displayName}</label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                        <input type="hidden" name="moduleId" value="${module.id}"/>
                                        <input type="hidden" name="subcorpus" value="false"/>
                                        <button
                                                class="btn waves-effect waves-light" type="submit">Launch
                                        </button>
                                    </form>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
                <c:if test="${modules.size() == 0}">
                    No modules available :(
                </c:if>

            </div>
        </div>
        <div class="row">
            <div class="col s12">
                <h4>Launch chained treatements</h4>
                <c:if test="${modules.size() != 0}">
                    <form method="post" id="formChain" action="<c:url value="/module/request-chained"/>">
                        <div class="row">
                            <div class="input-field s11">
                                <select name="corpusId">
                                    <option value="" disabled selected>Choose your option</option>
                                    <c:forEach var="corpu" items="${corpus}">
                                        <option value="${corpu.id}">${corpu.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col s6">
                                <ul class="collapsible popout" data-collapsible="accordion" id="myModules">
                                    <c:forEach items="${modules}" var="module">
                                        <li id="${module.id}">
                                            <div class="collapsible-header">
                                                <i class="material-icons">settings</i>${module.name} <a href="#!" class="secondary-content"><i class="material-icons chainModule">keyboard_arrow_right</i></a>
                                            </div>
                                            <div class="collapsible-body" style="padding: 2rem;">
                                                <c:forEach items="${module.params}" var="paramModule">
                                                    <div class="row">
                                                        <div class="input-field col s12">
                                                            <input id="input2${paramModule.id}" name="mod${module.id}.${paramModule.name}" type="${paramModule.typeHTML}" />
                                                            <label for="inpu2t${paramModule.id}">${paramModule.displayName}</label>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                            <div class="col s12">
                                <ul class="collapsible popout" data-collapsible="accordion" id="selectedModules">
                                </ul>
                            </div>
                        </div>
                        <div class="row">
                            <input type="hidden" id="modulesIDS" name="moduleId" value="" />
                            <button style="margin-left: 20px;"
                                    class="btn waves-effect waves-light" type="submit"
                                    onclick="initModules()" id="submitChain">Launch</button>
                        </div>
                    </form>
                </c:if>
                <c:if test="${modules.size() == 0}">
                    No modules available :(
                </c:if>
            </div>
        </div>
    </jsp:body>
</t:wrapper>