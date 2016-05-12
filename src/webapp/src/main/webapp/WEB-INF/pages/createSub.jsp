<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<t:wrapper title="Create Sub Corpus">
	<jsp:attribute name="header">
     	<script type="text/javascript">
			$(document).ready(function() {
				$('select').material_select();
				$('.datepicker').pickadate({
					selectMonths: true, // Creates a dropdown to control month
					selectYears: 15 // Creates a dropdown of 15 years to control year
				});
				$('.timepicker').pickatime();

			});
		</script>
    </jsp:attribute>
	<jsp:body>
        <div class="row">
            <form class="col s12" action="<c:url value="/sub"/>" method="post">
				<div class="row">
					<p>This interface allows you to isolate a specific part of your corpus by creating a sub-corpus. You can choose from one or several options, like dates, key words, hashtags or mentions present in the tweets.</p>
				</div>
				<br>
                <div class="row">
                	<div class="input-field s12">
					    <select name="corpusId" required>
					      <option value="" disabled selected>Choose your corpus</option>
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
					<label for="name">Start Date (Note that if you only select a start date, all the tweets after this date will be kept)</label>
					<input type="date" name="startDate" class="datepicker">
				</div>
				<div class="row">
					<label for="name">End Date (Note that if you only select an end date, all the tweets before this date will be kept)</label>
					<input type="date" name="endDate" class="datepicker" >
				</div>
                <div class="row">
                	<label for="regexp">Regular Expression (e.g. .+ing[,;:?!\s] will match words ending with 'ing' ; See <a href="http://www.vogella.com/tutorials/JavaRegularExpressions/article.html" target="_blank">this page</a> to learn more about regular expressions; We recommend using this online regex <a href="https://regex101.com" target="_blank">tester</a>)</label>
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