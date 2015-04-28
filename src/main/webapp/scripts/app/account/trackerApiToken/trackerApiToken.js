'use strict';

angular.module('nimblecalApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('trackerApiToken', {
                parent: 'account',
                url: '/trackerApiToken',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'trackerApiToken.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/trackerApiToken/trackerApiToken.html',
                        controller: 'TrackerApiTokenController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('trackerApiToken');
                        return $translate.refresh();
                    }]
                }
            });
    });
