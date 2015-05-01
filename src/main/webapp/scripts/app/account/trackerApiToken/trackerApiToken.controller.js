'use strict';

angular.module('nimblecalApp')
    .controller('TrackerApiTokenController', function ($rootScope, $scope, $http, Principal) {

        Principal.identity().then(function(account) {
            $scope.acct = account;
        });

        $scope.trackerApiToken = function () {
            $scope.trackerApiSuccess = false;
            $scope.trackerApiError = false;

            var config = {
                headers:  {
                'X-TrackerToken': $scope.token
            }
            };

            $http.get('https://www.pivotaltracker.com/services/v5/my/activity', config).
                success(function(data, status, headers, config) {
                    $scope.trackerApiSuccess = true;
                }).
                error(function(data, status, headers, config) {
                    $scope.trackerApiError = true;
                });
        };
    });
