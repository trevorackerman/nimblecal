'use strict';

describe('Tracker API Token Controller ', function () {
    var httpMock, $scope, cookieStore;

    beforeEach(module('nimblecalApp'));

    beforeEach(inject(function($rootScope, $controller, $httpBackend, $cookieStore) {
        cookieStore = $cookieStore;
        spyOn(cookieStore, 'put');
        spyOn(cookieStore, 'remove');
        spyOn(cookieStore, 'get');
        httpMock = $httpBackend;
        $scope = $rootScope.$new();
    }));

    function otherStuff() {
        // WTF is all this?....
        httpMock.expectGET(/api\/account\?cacheBuster=.*/).respond(200, '');
        httpMock.expectGET('i18n/en/global.json').respond(200, '');
        httpMock.expectGET('i18n/en/language.json').respond(200, '');
        httpMock.expectGET('scripts/components/navbar/navbar.html').respond(200, '');
        httpMock.expectGET('i18n/en/global.json').respond(200, '');
        httpMock.expectGET('i18n/en/language.json').respond(200, '');
        httpMock.expectGET('i18n/en/main.json').respond(200, '');
        httpMock.expectGET('scripts/app/main/main.html').respond(200, '');
    }

    describe('when user has not already entered a token', function () {
        beforeEach(inject(function($controller){
            $controller('TrackerApiTokenController', {$scope: $scope});
        }));

        it('calls tracker api to validate token', function() {
            httpMock.expectGET('https://www.pivotaltracker.com/services/v5/my/activity',
                function(headers) {
                    return headers['X-TrackerToken'] == 'goodtoken';
                })
                .respond(200, '');
            otherStuff();

            $scope.token = 'goodtoken';
            $scope.validateToken();
            httpMock.flush();
            expect($scope.trackerApiSuccess).toBeTruthy();
            expect($scope.trackerApiError).toBeFalsy();
        });

        it('indicates when the api token is not valid', function() {
            httpMock.expectGET('https://www.pivotaltracker.com/services/v5/my/activity',
                function(headers) {
                    return headers['X-TrackerToken'] == 'bad';
                })
                .respond(403, '');
            otherStuff();
            $scope.token = 'bad';
            $scope.validateToken();
            httpMock.flush();
            expect($scope.trackerApiError).toBeTruthy();
            expect($scope.trackerApiSuccess).toBeFalsy();
        });

        it ('stores tracker api token in session', function () {
            httpMock.expectGET('https://www.pivotaltracker.com/services/v5/my/activity')
                .respond(200, '');
            otherStuff();

            $scope.token = 'myTrackerToken';
            $scope.validateToken();
            httpMock.flush();
            expect(cookieStore.put).toHaveBeenCalledWith('trackerApiToken', 'myTrackerToken');
            expect(cookieStore.remove).toHaveBeenCalledWith('trackerApiToken');
        });
    });

    describe('when user has already entered a token', function () {
        beforeEach(inject(function($controller) {
            cookieStore.put('trackerApiToken', 'existingToken');
            $controller('TrackerApiTokenController', {$scope: $scope});
        }));

        it('indicates they may change their token', function () {
            expect(cookieStore.get).toHaveBeenCalledWith('trackerApiToken');
            expect(cookieStore.put).not.toHaveBeenCalledWith('trackerApiToken');
            expect(cookieStore.remove).not.toHaveBeenCalledWith('trackerApiToken');
        })
    })
});
