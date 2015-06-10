'use strict';

angular.module('nimblecalApp')
    .controller('MainController', function ($scope, Principal) {
        $scope.eventSources = [];
        $scope.isMain = true;
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
