'use strict';

angular.module('nimblecalApp')
    .controller('GithubFeedController', function ($scope, GithubFeed, ProjectFeed) {
        $scope.gitHubFeeds = [];
        $scope.projectFeeds = ProjectFeed.query();
        $scope.loadAll = function() {
            GithubFeed.query(function(result) {
               $scope.gitHubFeeds = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            GithubFeed.get({id: id}, function(result) {
                $scope.gitHubFeed = result;
                $('#saveGithubFeedModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.gitHubFeed.id != null) {
                GithubFeed.update($scope.gitHubFeed,
                    function () {
                        $scope.refresh();
                    });
            } else {
                GithubFeed.save($scope.gitHubFeed,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            GithubFeed.get({id: id}, function(result) {
                $scope.gitHubFeed = result;
                $('#deleteGithubFeedConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            GithubFeed.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteGithubFeedConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveGithubFeedModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.gitHubFeed = {repositoryURL: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
