(function() {

    var CONFIG = {
        ROLES: {
            USER: 'ROLE_USER',
            ADMIN: 'ROLE_ADMIN'
        }
    };

    var application = angular.module("todo.app",
        [
            "ngResource",
            "todo.data-task" ,
            "todo.data-user",
            "todo.nav-template",
            "todo.task-template",
            'todo.create-template'
        ]
    );

    var taskController = function($scope, $rootScope, taskService) {
        $scope.tasks = taskService.list();

        $rootScope.$on('callCompleted', function(event, args) {
           $scope.tasks.push(args);
        });

        $rootScope.$on('addNewTask', function(event, args) {
            if (args !== undefined && args !== undefined && args[0] !== undefined) {
                var newObj = {title: args[0].title, details: ""};

                taskService.create(newObj, function(result) {
                    $scope.tasks.push(result);
                })

            }
        });

        $rootScope.$on('taskCompleted', function(event, args) {
            try {
                if (args !== undefined && args !== null && args[0] !== undefined) {
                    var task = args[0];
                    var fn = args[1];
                    taskService.update(task, fn);
                }
            } catch (error) {
                console.log(error);
            }
        });
    };

    application.controller('taskController', ['$scope', '$rootScope', 'taskService', taskController]);

})();