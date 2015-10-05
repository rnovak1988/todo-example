(function() {

    var application = angular.module("todo.app", ["ngRoute", "ngResource"]);


    application.config(["$routeProvider", "$httpProvider", function($routeProvider, $httpProvider) {

         $routeProvider
             .when('/', {
                 'template': '<div>{{tasks | json}}</div>',
                 'controller': 'taskController'
             })
             .otherwise({
                 redirectTo: '/'
             });

    }]);


    var taskController = function($scope, taskService) {
        $scope.tasks = taskService.list();

        $scope.emptyTask = function() {
            return {title: "", details: ""};
        };

        $scope.temp = $scope.emptyTask();

        $scope.createNewTask = function() {
            taskService.create($scope.temp, function(data) {
                console.log(data);
            });
        };

    };

    var taskService = function($resource) {
        this.tasks =  $resource("/data/task", {}, {
            create: {method: 'PUT'},
            update: {method: 'POST'}
        });
        this.taskList = $resource("/data/task/list");
    };

    taskService.prototype.isFn = function(callback) {
        return (callback !== undefined && callback !== null && typeof callback === 'function');
    };

    taskService.prototype.get = function(id, callback) {
        var result = this.tasks.get({id: id});
        if (this.isFn(callback)) result.$promise.then(callback);
        return result;
    };

    taskService.prototype.list = function(callback) {
        var result = this.taskList.query();
        if (this.isFn(callback)) result.$promise.then(callback);
        return result;
    };

    taskService.prototype.create = function(params, callback) {
        console.log(params);
        var result = this.tasks.create(params);
        if (this.isFn(callback)) result.$promise.then(callback);
        return result;
    };

    application.service('taskService', ['$resource', taskService]);
    application.controller('taskController', ['$scope', 'taskService', taskController]);


})();