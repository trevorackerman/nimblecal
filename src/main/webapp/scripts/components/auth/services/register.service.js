'use strict';

angular.module('nimblecalApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


