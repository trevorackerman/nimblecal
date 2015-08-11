'use strict';

angular.module('nimblecalApp')
    .controller('GithubFeedDetailController', function ($scope, $stateParams, GithubFeed) {
        $scope.gitHubFeed = {};
        $scope.load = function (id) {
            GithubFeed.get({id: id}, function(result) {
              $scope.gitHubFeed = result;
            });
        };
        $scope.load($stateParams.id);
    });
