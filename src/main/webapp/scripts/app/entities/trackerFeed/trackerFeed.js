'use strict';

angular.module('nimblecalApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('trackerFeed', {
                parent: 'entity',
                url: '/trackerFeed',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.trackerFeed.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/trackerFeed/trackerFeeds.html',
                        controller: 'TrackerFeedController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('trackerFeed');
                        return $translate.refresh();
                    }]
                }
            })
            .state('trackerFeedDetail', {
                parent: 'entity',
                url: '/trackerFeed/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.trackerFeed.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/trackerFeed/trackerFeed-detail.html',
                        controller: 'TrackerFeedDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('trackerFeed');
                        return $translate.refresh();
                    }]
                }
            });
    });
