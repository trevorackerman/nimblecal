'use strict';

describe('ProjectFeed Controller ', function () {
    var controller, $scope, principalSpy, deferred, spyPromise, httpMock;

    beforeEach(module('nimblecalApp'));

    beforeEach(inject(function ($rootScope, $controller, $q, $httpBackend) {
        httpMock = $httpBackend;
        $scope = $rootScope.$new();
        deferred = $q.defer();

        spyPromise = deferred.promise;
        spyPromise.success = function(fn) {
            spyPromise.then(function (data) {
                fn(data);
            });
            return spyPromise;
        };

        inject(function ($rootScope, $controller, ProjectFeed, Principal, User) {
           principalSpy = spyOn(Principal, 'identity');
            principalSpy.and.returnValue(spyPromise);
           $controller('ProjectFeedController', {$scope: $scope, ProjectFeed: ProjectFeed, Principal:Principal, User:User});
       });

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

    it('initialy has currentUser undefined before any calls are made', function(){
        expect($scope.currentUser).toEqual(undefined);
    });

    it('gets the identity of the current user',function () {
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
        httpMock.expectGET(/api\/projectfeeds\?cacheBuster=.*/).respond(200, '');
        httpMock.expectGET(/api\/users\/sometester\?cacheBuster=.*/).respond(200, fakeUser);
        otherStuff();
        $scope.$apply();
        httpMock.flush();

    });
});
