'use strict';

angular.module('nimblecalApp')
    .controller('ProjectFeedsController', function ($scope, $http, ProjectFeed, TrackerFeed, ProjectFeedTrackerFeed, Principal, User) {
        $scope.projectFeeds = [];

        Principal.identity().then(function (account) {
            User.get({login:account.login})
                .$promise.then(function(user) {
                    $scope.currentUser = user;
                });
        });

        $scope.clear = function () {
            $scope.projectFeed = {title: null, id: null};
            $scope.trackerFeed = {projectId: null, id: null, projectFeed: $scope.projectFeed};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveProjectFeedsModal').modal('hide');
            $scope.clear();
        };

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
                $('#saveProjectFeedsModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.projectFeed.id != null) {
                ProjectFeed.update($scope.projectFeed,
                    function () {
                        $scope.refresh();
                    });
            } else {
                $scope.projectFeed.owner = $scope.currentUser;
                ProjectFeed.save($scope.projectFeed,
                    function (object, responseHeaders) {
                        $http.get(responseHeaders("Location")).success(
                            function (newProjectFeed) {
                                $scope.projectFeed = newProjectFeed;
                                $scope.trackerFeed.projectFeed = $scope.projectFeed;
                                TrackerFeed.save($scope.trackerFeed, function() {
                                    $scope.refresh();
                                });
                            });
                    });
            }
        };
});
