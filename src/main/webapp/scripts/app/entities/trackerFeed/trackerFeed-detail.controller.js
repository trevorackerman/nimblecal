'use strict';

angular.module('nimblecalApp')
    .controller('TrackerFeedDetailController', function ($scope, $stateParams, TrackerFeed) {
        $scope.trackerFeed = {};
        $scope.load = function (id) {
            TrackerFeed.get({id: id}, function(result) {
              $scope.trackerFeed = result;
            });
        };
        $scope.load($stateParams.id);
    });
