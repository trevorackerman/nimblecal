'use strict';

angular.module('nimblecalApp')
    .controller('MainController', function ($scope,ProjectFeed, Principal, User) {
        $scope.eventSources = [];
        $scope.isMain = true;

        $scope.loadAll = function() {
            ProjectFeed.query(function(result) {
                $scope.projectFeeds = result;
                $scope.selectedProjectFeed = $scope.projectFeeds[0];
            });
        };

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            if (account != null) {
                User.get({login:account.login})
                    .$promise.then(function(user) {
                        $scope.currentUser = user;
                    });
                $scope.loadAll();
            }
        });
    });
