'use strict';

angular.module('nimblecalApp')
    .controller('JiraFeedController', function ($scope, JiraFeed, ProjectFeed) {
        $scope.jiraFeeds = [];
        $scope.projectFeeds = ProjectFeed.query();
        $scope.loadAll = function() {
            JiraFeed.query(function(result) {
               $scope.jiraFeeds = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            JiraFeed.get({id: id}, function(result) {
                $scope.jiraFeed = result;
                $('#saveJiraFeedModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.jiraFeed.id != null) {
                JiraFeed.update($scope.jiraFeed,
                    function () {
                        $scope.refresh();
                    });
            } else {
                JiraFeed.save($scope.jiraFeed,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            JiraFeed.get({id: id}, function(result) {
                $scope.jiraFeed = result;
                $('#deleteJiraFeedConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            JiraFeed.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteJiraFeedConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveJiraFeedModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.jiraFeed = {site: null, projectId: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
