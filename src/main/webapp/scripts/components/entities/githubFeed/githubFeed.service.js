'use strict';

angular.module('nimblecalApp')
    .factory('GitHubFeed', function ($resource) {
        return $resource('api/gitHubFeeds/:id', {}, {
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
