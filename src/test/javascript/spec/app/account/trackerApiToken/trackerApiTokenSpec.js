'use strict';

describe('Tracker API Token Tests ', function () {

    beforeEach(module('nimblecalApp'));

    var $scope, q, Principal, Auth;

    // define the mock Auth service
    beforeEach(function() {
        Auth = {
            updateAccount: function() {}
        };

        Principal = {
            identity: function() {
                var deferred = q.defer();
                return deferred.promise;
            }
        };
    });


    describe('TrackerApiTokenController', function () {

    });
});
