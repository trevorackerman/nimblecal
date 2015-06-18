'use strict';

angular.module('nimblecalApp')
    .factory('TrackerEvents', function ($resource) {
        return $resource('api/trackerFeeds/:id/events', {}, {
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
