'use strict';

angular.module('nimblecalApp')
    .controller('JiraFeedDetailController', function ($scope, $stateParams, JiraFeed) {
        $scope.jiraFeed = {};
        $scope.load = function (id) {
            JiraFeed.get({id: id}, function(result) {
              $scope.jiraFeed = result;
            });
        };
        $scope.load($stateParams.id);
    });
