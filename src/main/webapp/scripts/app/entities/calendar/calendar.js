'use strict';

angular.module('nimblecalApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('calendar', {
                parent: 'entity',
                url: '/calendar',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.calendar.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/calendar/calendars.html',
                        controller: 'CalendarController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('calendar');
                        return $translate.refresh();
                    }]
                }
            })
            .state('calendarDetail', {
                parent: 'entity',
                url: '/calendar/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.calendar.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/calendar/calendar-detail.html',
                        controller: 'CalendarDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('calendar');
                        return $translate.refresh();
                    }]
                }
            });
    });
