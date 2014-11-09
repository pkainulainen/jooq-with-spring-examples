'use strict';

angular.module('app.search.services', ['ngResource'])
    .factory('Search', ['$resource', function($resource) {
        var api = $resource('/api/todo/search', {}, {
            'query':  {method:'GET', isArray:false}
        });

        return {
            findBySearchTerm: function(searchTerm, pageNumber, pageSize) {
                return api.query({page: pageNumber, searchTerm: searchTerm, size: pageSize, sort: 'ID,DESC'}).$promise;
            }
        };
    }]);