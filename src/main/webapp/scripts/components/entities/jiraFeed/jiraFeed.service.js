'use strict';

angular.module('nimblecalApp')
    .factory('JiraFeed', function ($resource) {
        return $resource('api/jiraFeeds/:id', {}, {
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
