'use strict';

angular.module('nimblecalApp')
    .controller('ProjectFeedDetailController', function ($scope, $stateParams, ProjectFeed) {
        $scope.projectFeed = {};
        $scope.load = function (id) {
            ProjectFeed.get({id: id}, function(result) {
              $scope.projectFeed = result;
            });
        };
        $scope.load($stateParams.id);
    });
