'use strict';

angular.module('nimblecalApp')
    .controller('GitHubFeedDetailController', function ($scope, $stateParams, GitHubFeed) {
        $scope.gitHubFeed = {};
        $scope.load = function (id) {
            GitHubFeed.get({id: id}, function(result) {
              $scope.gitHubFeed = result;
            });
        };
        $scope.load($stateParams.id);
    });
