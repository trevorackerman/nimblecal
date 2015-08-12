'use strict';

angular.module('nimblecalApp')
    .factory('GitHubEvents', function ($resource) {
        return $resource('api/gitHubFeeds/:id/events', {}, {
            'get': {
                method: 'GET',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
        });
    });
