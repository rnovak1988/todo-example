(function() {

    var dataTask = angular.module("todo.data-task", ["ngResource"]);

    var taskService = function ($resource) {
        var self = this;
        this.data = $resource('/data/task', {}, {
            create: {method: 'PUT'},
            update: {method: 'POST'},
            read: {method: 'GET'},
            list: {url: '/data/task/list', method: 'GET', isArray: true}
        });

        this.remoteMethod = function (method, args, callback) {
            var result = this.data[method](args);
            if (callback !== undefined && callback !== null && typeof callback === 'function') result.$promise.then(callback);
            return result;
        };
    };


    taskService.prototype.create = function(obj, callback) {
        return this.remoteMethod('create', obj, callback);
    };

    taskService.prototype.update = function(obj, callback) {
        return this.remoteMethod('update', obj, callback);
    };

    taskService.prototype.get = function (id, callback) {
        return this.remoteMethod('read', id, callback);
    };

    taskService.prototype.list = function(callback) {
        return this.remoteMethod('list', undefined, callback);
    };

    dataTask.service('taskService', ['$resource', taskService]);

})();