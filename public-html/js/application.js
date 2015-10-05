(function() {

    var CONFIG = {
        ROLES: {
            USER: 'ROLE_USER',
            ADMIN: 'ROLE_ADMIN'
        }
    };

    var application = angular.module("todo.app", ["ngRoute", "ngResource"]);

    application.config(["$routeProvider", "$httpProvider", function($routeProvider, $httpProvider) {

         $routeProvider
             .when('/', {
                 'template': '<div>{{tasks}}</div>',
                 'controller': 'taskController'
             })
             .otherwise({
                 redirectTo: '/'
             });

    }]);

    var formController = function($scope, $rootScope, taskService) {
        $scope.emptyTask = function() {
            return {title: "", details: ""};
        };

        $scope.temp = $scope.emptyTask();

        $scope.createNewTask = function() {
            taskService.create($scope.temp, function(data) {
                $scope.temp = $scope.emptyTask();
                $rootScope.$emit('callCompleted', data);
            });
        };
    };

    var userService = function($resource) {
        this.user = $resource('/user/', {}, {
            current: {method: 'GET', url: '/user/current'},
            currentRole: {method: 'GET', url: '/user/current/role'}
        });
    };

    userService.prototype.getCurrentUser = function(callback) {
        var user = this.user.current();

        if (callback !== undefined && callback !== null && typeof callback === 'function') user.$promise.then(callback);

        return user;
    };

    userService.prototype.getCurrentRole = function(callback) {
        var role = this.user.currentRole();

        if (callback !== undefined && callback !== null && typeof callback === 'function') role.$promise.then(callback);

        return role;
    };

    var topController = function($scope, $rootScope, userService) {
        $scope.currentUser = userService.getCurrentUser();
        $scope.currentRole = userService.getCurrentRole();

        $scope.is_admin = false;

        $scope.currentRole.$promise.then(function(role) {
            try {
                $scope.is_admin = (role.role === CONFIG.ROLES.ADMIN);
            } catch (fuckYou) {
                $scope.is_admin = false;
            }
        });
    };


    var taskController = function($scope, $rootScope, taskService) {
        $scope.tasks = taskService.list();

        $rootScope.$on('callCompleted', function(event, args) {
           $scope.tasks.push(args);
        });
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
        var result = this.tasks.create(params);
        if (this.isFn(callback)) result.$promise.then(callback);
        return result;
    };

    application.service('taskService', ['$resource', taskService]);
    application.service('userService', ['$resource', userService]);
    application.controller('taskController', ['$scope', '$rootScope', 'taskService', taskController]);
    application.controller('formController', ['$scope', '$rootScope', 'taskService', formController]);
    application.controller('topController', ['$scope', '$rootScope', 'userService', topController]);


})();