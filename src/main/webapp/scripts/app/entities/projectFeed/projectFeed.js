'use strict';

angular.module('nimblecalApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('projectFeed', {
                parent: 'entity',
                url: '/projectFeed',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.projectFeed.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/projectFeed/projectFeeds.html',
                        controller: 'ProjectFeedController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('projectFeed');
                        return $translate.refresh();
                    }]
                }
            })
            .state('projectFeedDetail', {
                parent: 'entity',
                url: '/projectFeed/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.projectFeed.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/projectFeed/projectFeed-detail.html',
                        controller: 'ProjectFeedDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('projectFeed');
                        return $translate.refresh();
                    }]
                }
            });
    });
