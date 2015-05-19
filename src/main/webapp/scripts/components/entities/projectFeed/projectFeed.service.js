'use strict';

angular.module('nimblecalApp')
    .factory('ProjectFeed', function ($resource) {
        return $resource('api/projectfeeds/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
