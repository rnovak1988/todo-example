(function() {

    var directive = angular.module("todo.nav-bar", ["ngRoute", "ngResource", "todo.data-user"]);

    var navController = function($scope, userService) {

    };

    var navDirective = function() {
        return {
            templateUrl: "/templates/navbar-template.html",
            controller: "navController"
        };
    };

    directive.controller("navController", ["$scope", "userService", navController]);
    directive.directive("navbar", navDirective);

})();