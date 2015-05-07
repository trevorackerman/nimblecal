'use strict';

angular.module('nimblecalApp')
    .controller('TrackerFeedController', function ($scope, TrackerFeed, Calendar) {
        $scope.trackerFeeds = [];
        $scope.calendars = Calendar.query();
        $scope.loadAll = function() {
            TrackerFeed.query(function(result) {
               $scope.trackerFeeds = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            TrackerFeed.get({id: id}, function(result) {
                $scope.trackerFeed = result;
                $('#saveTrackerFeedModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.trackerFeed.id != null) {
                TrackerFeed.update($scope.trackerFeed,
                    function () {
                        $scope.refresh();
                    });
            } else {
                TrackerFeed.save($scope.trackerFeed,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            TrackerFeed.get({id: id}, function(result) {
                $scope.trackerFeed = result;
                $('#deleteTrackerFeedConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TrackerFeed.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTrackerFeedConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveTrackerFeedModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.trackerFeed = {projectId: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
