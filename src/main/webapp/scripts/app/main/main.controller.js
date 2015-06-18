'use strict';

angular.module('nimblecalApp')
    .controller('MainController', function ($scope, ProjectFeed, TrackerEvents, Principal, User, localStorageService, uiCalendarConfig) {
        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();

        $scope.showExample = false;
        $scope.events = [[]];
        $scope.eventSources = [ $scope.events ];

        $scope.trackerApiToken = "";

        $scope.isMain = true;

        $scope.loadAll = function() {
            ProjectFeed.query(function(result) {
                $scope.projectFeeds = result;
                $scope.selectedProjectFeed = $scope.projectFeeds[0];

                TrackerEvents.get({id: $scope.selectedProjectFeed.trackerFeeds[0].id}, function(result) {
                    for (var i=0; i < result.length; i++) {
                        $scope.events.push(result[i]);
                    }
                });
            });
        };

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;

            if (account == null) {
                account = {
                  login: "anonymousUser"
                };
            }

            User.get({login:account.login})
                .$promise.then(function(user) {
                    $scope.currentUser = user;
                });
            $scope.trackerApiToken = localStorageService.get('trackerApiToken');
            $scope.loadAll();
        });

        $scope.updateTrackerApiToken = function(newToken) {
            localStorageService.set('trackerApiToken', newToken);
        };

        $scope.toggleExample = function() {
            $scope.showExample = $scope.showExample === false ? true: false;

            if ($scope.showExample) {
                $('#exampleToggle').text("Hide Example");
            }
            else {
                $('#exampleToggle').text("Show Example");
            }
        };

        $scope.renderCalendar = function () {
            uiCalendarConfig.calendars['projectCalendar'].fullCalendar('rerenderEvents');
        }
    });
