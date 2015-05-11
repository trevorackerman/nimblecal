'use strict';

angular.module('nimblecalApp')
    .controller('CalendarController', function ($scope, Calendar, Principal, User) {
        $scope.calendars = [];

        Principal.identity().then(function (account) {
            User.get({login:account.login})
                .$promise.then(function(user) {
                    $scope.currentUser = user;
                });
        });

        $scope.loadAll = function() {
            Calendar.query(function(result) {
               $scope.calendars = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Calendar.get({id: id}, function(result) {
                $scope.calendar = result;
                $('#saveCalendarModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.calendar.id != null) {
                Calsendar.update($scope.calendar,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Calendar.save($scope.calendar,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Calendar.get({id: id}, function(result) {
                $scope.calendar = result;
                $('#deleteCalendarConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Calendar.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCalendarConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveCalendarModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.calendar = {title: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
