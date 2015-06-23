'use strict';

angular.module('nimblecalApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jiraFeed', {
                parent: 'entity',
                url: '/jiraFeed',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nimblecalApp.jiraFeed.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jiraFeed/jiraFeeds.html',
                        controller: 'JiraFeedController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jiraFeed');
                        return $translate.refresh();
                    }]
                }
            })
            .state('jiraFeedDetail', {
                parent: 'entity',
                url: '/jiraFeed/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.jiraFeed.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jiraFeed/jiraFeed-detail.html',
                        controller: 'JiraFeedDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jiraFeed');
                        return $translate.refresh();
                    }]
                }
            });
    });
