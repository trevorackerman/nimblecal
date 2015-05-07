'use strict';

angular.module('nimblecalApp')
    .controller('GithubFeedController', function ($scope, GithubFeed, Calendar) {
        $scope.githubFeeds = [];
        $scope.calendars = Calendar.query();
        $scope.loadAll = function() {
            GithubFeed.query(function(result) {
               $scope.githubFeeds = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            GithubFeed.get({id: id}, function(result) {
                $scope.githubFeed = result;
                $('#saveGithubFeedModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.githubFeed.id != null) {
                GithubFeed.update($scope.githubFeed,
                    function () {
                        $scope.refresh();
                    });
            } else {
                GithubFeed.save($scope.githubFeed,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            GithubFeed.get({id: id}, function(result) {
                $scope.githubFeed = result;
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
            $scope.githubFeed = {repositoryURL: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
