'use strict';

angular.module('nimblecalApp')
    .controller('CalendarDetailController', function ($scope, $stateParams, Calendar, User) {
        $scope.calendar = {};
        $scope.load = function (id) {
            Calendar.get({id: id}, function(result) {
              $scope.calendar = result;
            });
        };
        $scope.load($stateParams.id);
    });
