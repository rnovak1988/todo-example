(function() {

    var application = angular.module("todo.app", ["ngRoute", "ngResource"]);


    application.config(["$routeProvider", function($routeProvider) {
         $routeProvider
             .when('/', {
                 'template': '<div>Hello World</div>'
             })
             .otherwise({
                 redirectTo: '/'
             })
    }]);


    var todoController = function($scope) {
        
    }


})();