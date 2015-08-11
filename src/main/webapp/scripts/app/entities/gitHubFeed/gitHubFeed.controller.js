'use strict';

angular.module('nimblecalApp')
    .controller('GitHubFeedController', function ($scope, GitHubFeed, ProjectFeed) {
        $scope.gitHubFeeds = [];
        $scope.projectFeeds = ProjectFeed.query();
        $scope.loadAll = function() {
            GitHubFeed.query(function(result) {
               $scope.gitHubFeeds = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            GitHubFeed.get({id: id}, function(result) {
                $scope.gitHubFeed = result;
                $('#saveGitHubFeedModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.gitHubFeed.id != null) {
                GitHubFeed.update($scope.gitHubFeed,
                    function () {
                        $scope.refresh();
                    });
            } else {
                GitHubFeed.save($scope.gitHubFeed,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            GitHubFeed.get({id: id}, function(result) {
                $scope.gitHubFeed = result;
                $('#deleteGitHubFeedConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            GitHubFeed.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteGitHubFeedConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveGitHubFeedModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.gitHubFeed = {repositoryURL: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
