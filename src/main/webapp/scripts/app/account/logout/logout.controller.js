'use strict';

angular.module('nimblecalApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
