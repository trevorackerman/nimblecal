'use strict';

angular.module('nimblecalApp')
    .factory('BitbucketFeed', function ($resource) {
        return $resource('api/bitbucketFeeds/:id', {}, {
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
