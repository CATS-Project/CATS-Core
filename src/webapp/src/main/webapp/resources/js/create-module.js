/**
 * Created by Nathanael on 04/02/2016.
 */
var myApp = angular.module('module-form',[]);

// Array Remove - By John Resig (MIT Licensed)
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};

myApp.controller('parameter', ['$scope', function($scope) {
    $scope.list = [];

    $scope.add = function(){
        if ($scope.current.name !== '' && $scope.current.displayName !=='' && $scope.current.type !== '') {
            $scope.list.push(jQuery.extend({}, $scope.current));
            $scope.current.name = '';
            $scope.current.displayName = '';
            $scope.current.type = '';
        }
    };
    $scope.remove = function (i) {
        $scope.list.remove(i);
    }
}]);