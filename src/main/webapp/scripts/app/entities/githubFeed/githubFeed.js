'use strict';

angular.module('nimblecalApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('gitHubFeed', {
                parent: 'entity',
                url: '/gitHubFeed',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nimblecalApp.gitHubFeed.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gitHubFeed/gitHubFeeds.html',
                        controller: 'GithubFeedController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('gitHubFeed');
                        return $translate.refresh();
                    }]
                }
            })
            .state('gitHubFeedDetail', {
                parent: 'entity',
                url: '/gitHubFeed/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.gitHubFeed.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gitHubFeed/gitHubFeed-detail.html',
                        controller: 'GithubFeedDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('gitHubFeed');
                        return $translate.refresh();
                    }]
                }
            });
    });
