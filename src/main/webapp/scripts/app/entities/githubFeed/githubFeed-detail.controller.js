'use strict';

angular.module('nimblecalApp')
    .controller('GithubFeedDetailController', function ($scope, $stateParams, GithubFeed, Calendar) {
        $scope.githubFeed = {};
        $scope.load = function (id) {
            GithubFeed.get({id: id}, function(result) {
              $scope.githubFeed = result;
            });
        };
        $scope.load($stateParams.id);
    });
