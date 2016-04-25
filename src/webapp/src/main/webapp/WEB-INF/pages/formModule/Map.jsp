<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="row hide" data-ng-controller="map" id="divMap">
    <div class="input-field col s12">
        <div>
            <div id="map"></div>
            <input  type="hidden" name="location" readonly id="destinationLat"/>
            <input  type="hidden" name="location" readonly id="destinationLng"/>
            <input  type="hidden" name="location" readonly id="originLat"/>
            <input  type="hidden" name="location" readonly id="originLng"/>
        </div>
    </div>
</div>
