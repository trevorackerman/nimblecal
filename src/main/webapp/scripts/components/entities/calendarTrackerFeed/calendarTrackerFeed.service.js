'use strict';

angular.module('nimblecalApp')
    .factory('CalendarTrackerFeed', function ($resource) {
        return $resource('api/calendars/:id/trackerFeeds', {}, {
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
