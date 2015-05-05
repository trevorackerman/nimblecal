'use strict';

angular.module('nimblecalApp')
    .controller('TrackerApiTokenController', function ($rootScope, $scope, $http, $cookieStore) {
        $scope.trackerApiToken = $cookieStore.get('trackerApiToken');

        $scope.validateToken = function () {
            $scope.trackerApiSuccess = false;
            $scope.trackerApiError = false;
            $cookieStore.remove('trackerApiToken');

            var config = {
                headers: {
                    'X-TrackerToken': $scope.token
                }
            };

            $http.get('https://www.pivotaltracker.com/services/v5/my/activity', config).
                success(function (data, status, headers, config) {
                    $scope.trackerApiSuccess = true;
                    $cookieStore.put('trackerApiToken', $scope.token);
                }).
                error(function (data, status, headers, config) {
                    $scope.trackerApiError = true;
                });
        };
    });
