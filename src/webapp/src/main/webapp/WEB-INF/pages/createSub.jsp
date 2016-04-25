<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:wrapper title="Create Sub Corpus">
	<jsp:attribute name="header">
     	<script type="text/javascript">
			$(document).ready(function() {
				$('select').material_select();
			});
		</script>
    </jsp:attribute>
	<jsp:body>
        <div class="row">
            <form class="col s12" action="<c:url value="/sub"/>" method="post">
                <div class="row">
                	<div class="input-field s12">
					    <select name="corpusId">
					      <option value="" disabled selected>Choose your option</option>
					      <c:forEach var="corpus" items="${corpuses}">
						    <option value="${corpus.id}">${corpus.name}</option>
						  </c:forEach>
					    </select>
					    <label>Corpus</label>
					</div>
                </div>
                
                <div class="row">
                	<label for="name">Name</label>
                	<input id="name" type="text" class="validate" name="name">
                </div>
                
                <div class="row">
                	<label for="regexp">Regular Expression (e.g. .+ing[,;:?!\s] will match words ending with 'ing' ; See <a href="http://www.vogella.com/tutorials/JavaRegularExpressions/article.html">this page</a> to learn more about regular expression)</label>
                	<input id="regexp" type="text" class="validate" name="regexp">
                </div>
                
                <div class="row">
                	<label for="hashtags">Hashtags (A list of hashtags (without the leading #) separated with commas, e.g. OccupyWallStreet, FollowFriday)</label>
                	<input id="hashtags" type="text" class="validate" name="hashtags">
                </div>
                
                <div class="row">
                	<label for="mentions">Mentions (A list of mentions (without the leading @) separated with commas, e.g. JustinBieber, BarackObama)</label>
                	<input id="mentions" type="text" class="validate" name="mentions">
                </div>

				<label>During the processing, please don't close this page or refresh the browser. You'll be taken to the new sub-corpus page once it's ready.</label>
                <button class="btn waves-effect waves-light"
					type="submit">
                    Create Sub Corpus
                </button>
            </form>
        </div>
    </jsp:body>
</t:wrapper>