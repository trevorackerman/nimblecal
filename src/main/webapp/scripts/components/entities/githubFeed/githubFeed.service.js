'use strict';

angular.module('nimblecalApp')
    .factory('GithubFeed', function ($resource) {
        return $resource('api/githubFeeds/:id', {}, {
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
