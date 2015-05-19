'use strict';

angular.module('nimblecalApp')
    .controller('BitbucketFeedController', function ($scope, BitbucketFeed, ProjectFeed) {
        $scope.bitbucketFeeds = [];
        $scope.projectFeeds = ProjectFeed.query();
        $scope.loadAll = function() {
            BitbucketFeed.query(function(result) {
               $scope.bitbucketFeeds = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            BitbucketFeed.get({id: id}, function(result) {
                $scope.bitbucketFeed = result;
                $('#saveBitbucketFeedModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.bitbucketFeed.id != null) {
                BitbucketFeed.update($scope.bitbucketFeed,
                    function () {
                        $scope.refresh();
                    });
            } else {
                BitbucketFeed.save($scope.bitbucketFeed,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            BitbucketFeed.get({id: id}, function(result) {
                $scope.bitbucketFeed = result;
                $('#deleteBitbucketFeedConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            BitbucketFeed.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBitbucketFeedConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveBitbucketFeedModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.bitbucketFeed = {site: null, repositoryOwner: null, repositoryName: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
