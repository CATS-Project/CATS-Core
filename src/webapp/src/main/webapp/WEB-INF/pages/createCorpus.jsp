<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<t:wrapper title="Create corpus">
    <jsp:attribute name="header">
        <script src="<c:url value="/resources/js/angular.min.js"/>"></script>
        <script src="<c:url value="/resources/js/corpus-form.js"/>"></script>
        <script src="<c:url value="/resources/js/draw_map.js"/>"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('select').material_select();
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <ul class="collapsible" data-collapsible="accordion">
            <li>
                <div class="collapsible-header ${user.tokenOk ? "" : "active"}"><i class="material-icons">swap_vert</i>Import CSV corpus</div>
                <div class="collapsible-body" style="padding: 1rem;">
                    <div class="row">
                        <div class="col s12">
                            <p>
                                You should provide a CSV file (to learn more about the CSV format, see the
                                <a href="https://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a> article
                                for instance) that describes your corpus according to the following fields:
                            </p>

                            <ul style="margin-left:50px;">
                                <li>Id (mandatory, numerical)</li>
                                <li>AuthorId (mandatory, numerical)</li>
                                <li>Date (mandatory, YYYY-MM-DD HH:MM:SS)</li>
                                <li>Text (mandatory, text)</li>
                                <li>Location (optional, 2 longitude,latitude pairs separated by a space)</li>
                                <li>AuthorDescription (optional, text)</li>
                                <li>AuthorName (optional, text)</li>
                            </ul>

                            <p>
                                The first line of the file should be the following header: Id, AuthorId, Date, Text, Location, AuthorDescription, AuthorName.
                                The subsequent lines of the file should describe a document each.
                            </p>
                            <form action="<c:url value="/corpus/import"/>" enctype="multipart/form-data" method="post">
                                <div class="file-field input-field">
                                    <div class="btn">
                                        <span>File</span>
                                        <input type="file" name="file">
                                    </div>
                                    <div class="file-path-wrapper">
                                        <input class="file-path validate" type="text"/>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="input-field col s12">
                                        <input id="nameC" type="text" class="validate" name="name">
                                        <label for="nameC">New corpus name</label>
                                    </div>
                                </div>
                                <label>During the import, please don't close this page or refresh the browser. You'll be taken to the new corpus page once it's ready.</label>
                                <button class="btn waves-effect waves-light" type="submit" name="action">
                                    Submit
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </li>
            <c:if test="${user.tokenOk}">
                <li>
                    <div class="collapsible-header active"><i class="material-icons">trending_up</i>Collect tweets</div>
                    <div class="collapsible-body" style="padding: 1rem;"  data-ng-app="corpus-form">
                        <form class="col s12" action="<c:url value="/corpus"/>" method="post" data-submit-Listener>
                            <div class="row">
                                <div class="input-field col s12">
                                    <input id="duration" type="number" class="validate" name="duration">
                                    <label for="duration">Duration (number of days during which tweets should be collected, example
                                        "30")</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s12">
                                    <input id="name" type="text" class="validate" name="name">
                                    <label for="name">New corpora name</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s12">
                                    <select name="lang" id="lang">
                                        <option value="" disabled selected>Choose a language (Optional)</option>
                                        <option value="fr">French</option>
                                        <option value="en">English</option>
                                        <option value="ar">Arabic</option>
                                        <option value="ja">Japanese</option>
                                        <option value="es">Spanish</option>
                                        <option value="de">German</option>
                                        <option value="it">Italian</option>
                                        <option value="id">Indonesian</option>
                                        <option value="pt">Portuguese</option>
                                        <option value="ko">Korean</option>
                                        <option value="tr">Turkish</option>
                                        <option value="ru">Russian</option>
                                        <option value="nl">Dutch</option>
                                        <option value="fil">Filipino</option>
                                        <option value="msa">Malay</option>
                                        <option value="zh-tw">Traditional Chinese</option>
                                        <option value="zh-cn">Simplified Chinese</option>
                                        <option value="hi">Hindi</option>
                                        <option value="no">Norwegian</option>
                                        <option value="sv">Swedish</option>
                                        <option value="fi">Finnish</option>
                                        <option value="da">Danish</option>
                                        <option value="pl">Polish</option>
                                        <option value="fa">Farsi</option>
                                        <option value="he">Hebrew</option>
                                        <option value="ur">Urdu</option>
                                        <option value="th">Thai</option>
                                        <option value="en-gb">English UK</option>
                                    </select>
                                    <label for="lang">Language</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s12">
                                    <label>Filter: </label><br>
                                </div>
                                <div class="input-field col s12">
                                    <input name="filter" type="radio" id="Map" data-ng-model="filter" value="map"/>
                                    <label for="Map">Map</label>
                                    <input name="filter" type="radio" id="User" value="user"/>
                                    <label for="User">User</label>
                                    <input name="filter" type="radio" id="Keywords" value="keyword"/>
                                    <label for="Keywords">Keywords</label>
                                </div>
                            </div>
                            <c:forEach items="${forms}" var="field">
                                <jsp:include page="${field.value}" />
                            </c:forEach>

                            <button class="btn waves-effect waves-light" type="submit">
                                Collect tweets
                            </button>

                        </form>
                    </div>
                </li>
            </c:if>
        </ul>
        <script type="text/javascript">

            $("input[name='filter']").on("change", function(){
                var filter = $('input[name=filter]:checked').val();
                console.log(filter);
                if($('input[name=filter]:checked').val() == "map"){
                    $("#divMap").removeClass("hide");
                    $("#divUser").addClass("hide");
                    $("#divKey").addClass("hide");
                    initMap();
                }
                if($('input[name=filter]:checked').val() == "user"){
                    $("#divUser").removeClass("hide");
                    $("#divMap").addClass("hide");
                    $("#divKey").addClass("hide");
                }
                if($('input[name=filter]:checked').val() == "keyword"){
                    $("#divKey").removeClass("hide");
                    $("#divMap").addClass("hide");
                    $("#divUser").addClass("hide");
                }

            })
        </script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDg3Kf8HbtHpqVtSoqybLSx_dzFxodJxsM&signed_in=true&libraries=drawing"
                async defer></script>
    </jsp:body>
</t:wrapper>