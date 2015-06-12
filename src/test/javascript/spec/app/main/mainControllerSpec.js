'use strict';

describe('Main Controller ', function () {
    var controller, $scope, principalSpy, deferred, spyPromise, httpMock, editFormSpy;

    beforeEach(module('nimblecalApp'));

    beforeEach(inject(function ($rootScope, $controller, $q, $httpBackend) {
        httpMock = $httpBackend;
        $scope = $rootScope.$new();
        deferred = $q.defer();

        spyPromise = deferred.promise;
        spyPromise.success = function (fn) {
            spyPromise.then(function (data) {
                fn(data);
            });
            return spyPromise;
        };


        inject(function ($rootScope, $controller, ProjectFeed, TrackerFeed, Principal, User) {
            principalSpy = spyOn(Principal, 'identity');
            principalSpy.and.returnValue(spyPromise);
            $controller('MainController',
                { $scope: $scope, ProjectFeed: ProjectFeed, Principal: Principal, User: User}
            );
        });

        var fakeUser = {
            'id': 256,
            'email': 'someone@example.com',
            'firstName' : 'someone',
            'lastName' : 'tester',
            'login' : 'sometester',
            'password' : null
        };

        var fakePrincipal = {
            'email': 'someone@example.com',
            'firstName' : 'someone',
            'lastName' : 'tester',
            'login' : 'sometester',
            'password' : null,
            'roles' : ['USER_ROLE']
        };

        deferred.resolve(fakePrincipal);
        httpMock.expectGET(/api\/users\/sometester\?cacheBuster=.*/).respond(200, fakeUser);


    }));

    function otherStuff() {
        // WTF is all this?....
        httpMock.expectGET('i18n/en/global.json').respond(200, '');
        httpMock.expectGET('i18n/en/language.json').respond(200, '');
        httpMock.expectGET('scripts/components/navbar/navbar.html').respond(200, '');
        httpMock.expectGET('i18n/en/global.json').respond(200, '');
        httpMock.expectGET('i18n/en/language.json').respond(200, '');
        httpMock.expectGET('i18n/en/main.json').respond(200, '');
        httpMock.expectGET('scripts/app/main/main.html').respond(200, '');
    }

    it('fetches project feeds for the current user', function() {
        httpMock.expectGET(/api\/projectfeeds\?cacheBuster=.*/).respond(200, [
            { title: 'something wonderful', id: 1, trackerFeeds: [ {id : 1, projectId: 23452345} ] }
        ]);
        otherStuff();

        $scope.$apply();
        httpMock.flush();
        httpMock.verifyNoOutstandingRequest();
        expect($scope.projectFeeds.length).toBe(1);
    });
});
