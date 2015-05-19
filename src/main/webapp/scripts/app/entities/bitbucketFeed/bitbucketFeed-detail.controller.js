'use strict';

angular.module('nimblecalApp')
    .controller('BitbucketFeedDetailController', function ($scope, $stateParams, BitbucketFeed) {
        $scope.bitbucketFeed = {};
        $scope.load = function (id) {
            BitbucketFeed.get({id: id}, function(result) {
              $scope.bitbucketFeed = result;
            });
        };
        $scope.load($stateParams.id);
    });
