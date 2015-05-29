'use strict';

angular.module('nimblecalApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('projectFeeds', {
                parent: 'site',
                url: '/projectFeeds',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'projectFeeds.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/projectFeeds/projectFeeds.html',
                        controller: 'ProjectFeedsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('projectFeeds');
                        return $translate.refresh();
                    }]
                }
            });
    });
