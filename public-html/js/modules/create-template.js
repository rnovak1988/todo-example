(function() {

    var createTemplate = angular.module("todo.create-template", []);


    var createFactory = function() {
        return {
            templateUrl: '/templates/create-template.html',
            controller: 'createController',
            scope: {}
        };
    };

    var createController = function($scope, $rootScope) {

        function createTempObject() {
            return { title: "", details: ""};
        }

        $scope.temp = createTempObject();

        $scope.add_task = function() {
            var newTask = $scope.temp;
            $scope.temp = createTempObject();
            $rootScope.$emit('addNewTask', [newTask]);
        };

    };

    createTemplate.controller('createController', ['$scope', '$rootScope', createController]);
    createTemplate.directive('createTask', createFactory);

})();