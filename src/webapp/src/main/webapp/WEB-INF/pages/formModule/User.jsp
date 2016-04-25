<%--
  Created by IntelliJ IDEA.
  User: Nathanael
  Date: 30/11/2015
  Time: 08:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="row hide" id="divUser" data-ng-controller="KeyWordsController">
    <div class="input-field col s12">
        <h5>User filter</h5>
        <ul class="add-list">
            <li data-ng-repeat="n in keywords track by $index">
                <div class="row">
                    <div class="input-field col s9">
                        <input type="text" class="validate no-margin" name="user" data-ng-model="keywords[$index]" data-ng-focus="focus($index)" title="user"/>
                    </div>
                    <div class="input-field col s3" data-ng-click="remove($index)" style="cursor: pointer;">
                        <i class="material-icons" data-ng-show="$index!=0">close</i>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</div>
