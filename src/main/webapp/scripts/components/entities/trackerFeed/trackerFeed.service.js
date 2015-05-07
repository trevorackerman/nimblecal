'use strict';

angular.module('nimblecalApp')
    .factory('TrackerFeed', function ($resource) {
        return $resource('api/trackerFeeds/:id', {}, {
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
