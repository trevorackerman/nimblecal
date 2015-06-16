'use strict';

angular.module('nimblecalApp')
    .controller('MainController', function ($scope,ProjectFeed, Principal, User, localStorageService, uiCalendarConfig) {
        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();

        $scope.events = [
            {title: 'All Day Event',start: new Date(y, m, 1)},
            {title: 'Long Event',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2)},
            {id: 999,title: 'Repeating Event',start: new Date(y, m, d - 3, 16, 0),allDay: false},
            {id: 999,title: 'Repeating Event',start: new Date(y, m, d + 4, 16, 0),allDay: false},
            {title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false}
        ];

        $scope.showExample = false;
        $scope.eventSources = [ $scope.events ];
        $scope.trackerApiToken = "";

        $scope.isMain = true;

        $scope.loadAll = function() {
            ProjectFeed.query(function(result) {
                $scope.projectFeeds = result;
                $scope.selectedProjectFeed = $scope.projectFeeds[0];
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

        $scope.renderCalender = function(calendar) {
            if(uiCalendarConfig.calendars[calendar]){
                uiCalendarConfig.calendars[calendar].fullCalendar('render');
            }
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
    });
