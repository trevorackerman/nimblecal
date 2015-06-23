'use strict';

angular.module('nimblecalApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('bitbucketFeed', {
                parent: 'entity',
                url: '/bitbucketFeed',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'nimblecalApp.bitbucketFeed.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bitbucketFeed/bitbucketFeeds.html',
                        controller: 'BitbucketFeedController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bitbucketFeed');
                        return $translate.refresh();
                    }]
                }
            })
            .state('bitbucketFeedDetail', {
                parent: 'entity',
                url: '/bitbucketFeed/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'nimblecalApp.bitbucketFeed.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bitbucketFeed/bitbucketFeed-detail.html',
                        controller: 'BitbucketFeedDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bitbucketFeed');
                        return $translate.refresh();
                    }]
                }
            });
    });
