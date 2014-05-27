'use strict';

angular.module('app.todo.services', ['ngResource'])
    .factory('Todos', ['$resource', 'NotificationService', function($resource, NotificationService) {
        var api = $resource('/api/todo/:id', {"id": "@id"}, {
            query:  {method: 'GET', params: {}, isArray: true},
            get:    {method: 'GET'},
            save: {method: 'POST'},
            update: {method: 'PUT'}
        });

        return {
            delete: function(todo, successCallback) {
                return api.delete(todo, function() {
                    NotificationService.flashMessage('todo.notifications.delete.success', 'success');
                    successCallback();
                });
            },
            query: function() {
                return api.query();
            },
            get: function(todoId) {
                return api.get({id: todoId}).$promise;
            },
            save: function(todo, successCallback) {
                api.save(todo, function(added) {
                    NotificationService.flashMessage('todo.notifications.add.success', 'success');
                    successCallback(added);
                });
            },
            update: function(todo, successCallback) {
                api.update(todo, function(updated) {
                    NotificationService.flashMessage('todo.notifications.update.success', 'success');
                    successCallback(updated);
                });
            }
        };
    }]);