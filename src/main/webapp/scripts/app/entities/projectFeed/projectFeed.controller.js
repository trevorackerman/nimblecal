'use strict';

angular.module('nimblecalApp')
    .controller('ProjectFeedController', function ($scope, ProjectFeed, Principal, User, ProjectFeedTrackerFeed) {
        $scope.projectFeeds = [];

        Principal.identity().then(function (account) {
            User.get({login:account.login})
                .$promise.then(function(user) {
                    $scope.currentUser = user;
                });
        });

        $scope.loadAll = function() {
            ProjectFeed.query(function(result) {
               $scope.projectFeeds = result;
                angular.forEach($scope.projectFeeds, function(projectFeed, index) {
                    ProjectFeedTrackerFeed.query({id: projectFeed.id}, function(result) {
                        projectFeed.trackerFeeds = result;
                    });
                })

            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ProjectFeed.get({id: id}, function(result) {
                $scope.projectFeed = result;
                $('#saveProjectFeedModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.projectFeed.id != null) {
                ProjectFeed.update($scope.projectFeed,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ProjectFeed.save($scope.projectFeed,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ProjectFeed.get({id: id}, function(result) {
                $scope.projectFeed = result;
                $('#deleteProjectFeedConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ProjectFeed.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProjectFeedConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveProjectFeedModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.projectFeed = {title: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
