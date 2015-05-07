'use strict';

angular.module('nimblecalApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('githubFeed', {
                parent: 'entity',
                url: '/githubFeed',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.githubFeed.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/githubFeed/githubFeeds.html',
                        controller: 'GithubFeedController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('githubFeed');
                        return $translate.refresh();
                    }]
                }
            })
            .state('githubFeedDetail', {
                parent: 'entity',
                url: '/githubFeed/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.githubFeed.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/githubFeed/githubFeed-detail.html',
                        controller: 'GithubFeedDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('githubFeed');
                        return $translate.refresh();
                    }]
                }
            });
    });
