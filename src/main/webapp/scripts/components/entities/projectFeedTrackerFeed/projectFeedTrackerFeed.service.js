'use strict';

angular.module('nimblecalApp')
    .factory('ProjectFeedTrackerFeed', function ($resource) {
        return $resource('api/projectfeeds/:id/trackerFeeds', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
        });
    });
