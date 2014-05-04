'use strict';

angular.module('app.todo.services', ['ngResource'])
    .factory('Todos', ['$resource', function($resource) {
        return $resource('/api/todo/:id', {"id": "@id"}, {
            query:  {method: 'GET', params: {}, isArray: true},
            get:    {method: 'GET'},
            save: {method: 'POST'},
            update: {method: 'PUT'}
        });
    }]);