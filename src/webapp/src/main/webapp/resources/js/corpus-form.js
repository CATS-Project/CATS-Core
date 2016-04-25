var myApp = angular.module('corpus-form',[]);

// Array Remove - By John Resig (MIT Licensed)
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};

myApp.controller('KeyWordsController', ['$scope', function($scope) {

    $scope.keywords = [''];

    $scope.remove = function(i){
        $scope.keywords.remove(i);
    };

    $scope.focus = function(i){
        if(i == $scope.keywords.length-1)
            $scope.keywords.push('');
    };

}]);

myApp.directive('submitListener',function(){
    return {
        link: function(scope, element) {
            element.submit(function(){
                var $user = $("input[type=text][name=user]:last");
                if ($user.val() === "") $user.remove();
                var $keyword = $("input[type=text][name=keyword]:last");
                if ($keyword.val() === "") $keyword.remove();
            });
        }
    }
});
myApp.controller('map', ['$scope', function($scope) {
    $scope.$watch('mapShown', function(newValue, oldValue) {
        if (newValue !== oldValue) {
            setTimeout(function(){
                google.maps.event.trigger(document.map, 'resize');
            }, 2000);

        }
    });
}]);