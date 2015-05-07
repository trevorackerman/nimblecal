'use strict';

angular.module('nimblecalApp')
    .controller('CalendarController', function ($scope, Calendar, User) {
        $scope.calendars = [];
        $scope.users = User.query();
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
                Calendar.update($scope.calendar,
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
