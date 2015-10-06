(function() {

    var dataUser = angular.module("todo.data-user", ["ngResource"]);

    var userService = function($resource) {
        this.users = $resource("/user/", {}, {
            list: {method: 'GET', isArray: true, url: '/user/list'},
            create: {method: 'PUT', url: '/user/new'},
            currentUser: {method: 'GET', url: '/user/current'},
            currentRole: {method: 'GET', url: '/user/current/role'}
        });
    };

    userService.prototype.list = function(callback) {
        var list = this.users.list();
        if (callback !== undefined && callback !== null && typeof callback === 'function') list.$promise.then(callback);
        return list;
    };

    userService.prototype.create = function(newUser, callback) {
        var user = this.users.create(newUser);
        if (callback !== undefined && callback !== null && typeof callback === 'function') user.$promise.then(callback);
        return user;
    };

    userService.prototype.getCurrentUser = function(callback) {
        var user = this.users.currentUser();
        if (callback !== undefined && callback !== null && typeof callback === 'function') user.$promise.then(callback);
        return user;
    };

    userService.prototype.getCurrentRole = function(callback) {
        var role = this.users.currentRole();
        if (callback !== undefined && callback !== null && typeof callback === 'function') role.$promise.then(callback);
        return role;
    };

    dataUser.service('userService', ['$resource', userService]);

})();