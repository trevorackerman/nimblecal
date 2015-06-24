'use strict';

angular.module('nimblecalApp')
    .controller('MainController', function ($scope, ProjectFeed, TrackerEvents, Principal, User, localStorageService, uiCalendarConfig) {

        $scope.eventRender = function(event, element) {
            element.find('div.fc-content').prepend('<span class="badge fc-title-highlight">' + event.avatarAlternate + '</span>');
            element.popover({
                title: event.description,
                content: event.message,
                container: 'body',
                html: true
            });
        };

        $scope.uiConfig = {
            calendar:{
                eventRender: $scope.eventRender
            }
        };

        $scope.showExample = false;
        $scope.events = [[]];
        $scope.eventSources = [ $scope.events ];

        $scope.trackerApiToken = "";

        $scope.isMain = true;

        $scope.loadProjectFeed = function(projectFeed) {
            TrackerEvents.get({id: projectFeed.trackerFeeds[0].id}, function(result) {
                uiCalendarConfig.calendars.projectCalendar.fullCalendar('removeEventSource', $scope.events );
                $scope.events = result;
                uiCalendarConfig.calendars.projectCalendar.fullCalendar('addEventSource', $scope.events );
                uiCalendarConfig.calendars.projectCalendar.fullCalendar('refetchEvents');
            });
        };

        $scope.loadAll = function() {
            ProjectFeed.query(function(result) {
                $scope.projectFeeds = result;
                if ($scope.selectedProjectFeed != $scope.projectFeeds[0]) {
                    $scope.selectedProjectFeed = $scope.projectFeeds[0];
                    $scope.loadProjectFeed($scope.selectedProjectFeed);
                }
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
        };

        $scope.selectProjectFeed = function(projectFeed) {
            $scope.selectedProjectFeed = projectFeed;
            $scope.loadProjectFeed($scope.selectedProjectFeed);
        }
    });
