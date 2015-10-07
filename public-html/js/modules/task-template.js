(function() {
    var taskTemplates = angular.module("todo.task-template", []);

    var taskController = function($scope, $rootScope) {

        $scope.completeTask = function(task) {
            task.isCompleted = !task.isCompleted;
            $rootScope.$emit('taskCompleted', [task]);
        };

        $scope.archiveTask = function(task) {
            $rootScope.$emit('taskArchived', [task]);
        };

    };

    var taskListFactory = function() {
        return {
            templateUrl: '/templates/task-list-template.html',
            scope: {
                tasks : '=taskList'
            }
        };
    };

    var taskFactory = function() {
        return {
            templateUrl: '/templates/task-template.html',
            controller: 'taskTemplateController',
            scope: {
                task: '=task'
            }
        };
    };

    taskTemplates.controller('taskTemplateController', ['$scope', '$rootScope', taskController]);
    taskTemplates.directive('taskList', taskListFactory);
    taskTemplates.directive('task', taskFactory);

})();