'use strict';

describe('ProjectFeeds Controller ', function () {
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
            $controller('ProjectFeedsController',
                { $scope: $scope, ProjectFeed: ProjectFeed, TrackerFeed: TrackerFeed, Principal: Principal, User: User}
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
        httpMock.expectGET(/api\/projectfeeds\?cacheBuster=.*/).respond(200, '');
        httpMock.expectGET(/api\/users\/sometester\?cacheBuster=.*/).respond(200, fakeUser);
        otherStuff();
        $scope.$apply();
        httpMock.flush();

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

    it('save persists a project feed with an associated tracker feed', function() {
        httpMock.expectPOST(/api\/projectfeeds\?cacheBuster=.*/,
            {"title":"something wonderful",
                "id":null,
                "owner":{
                    "id":256,
                    "email":"someone@example.com",
                    "firstName":"someone",
                    "lastName":"tester",
                    "login":"sometester",
                    "password":null
                }
            })
            .respond(201,{}, {Location: 'api/projectfeeds/1'});

        httpMock.expectGET(/api\/projectfeeds\/1\?cacheBuster=.*/).respond(200, {title: 'something wonderful', id: 1});

        httpMock.expectPOST(/api\/trackerFeeds\?cacheBuster=.*/,
            {projectId: '442903', id: null, projectFeed: { id: 1, title: 'something wonderful'}}).respond(201, {}, {Location: 'api/trackerfeeds/2'});

        httpMock.expectPOST(/api\/gitHubFeeds\?cacheBuster=.*/, {
                id: null,
                repositoryURL: 'https://github.com/johnlithgow/harryandthehendersons',
                repositoryOwner: 'johnlithgow',
                repositoryName: 'harryandthehendersons',
                projectFeed: { id: 1, title: 'something wonderful'}
            }
        ).respond(201, {}, {Location: 'api/gitHubFeeds/2'});

        httpMock.expectGET(/api\/projectfeeds\?cacheBuster=.*/).respond(200);
        httpMock.expectGET(/api\/projectfeeds\?cacheBuster=.*/).respond(200);

        $scope.editForm = {};
        $scope.editForm.$setPristine = function() {};
        $scope.editForm.$setUntouched = function () {};

        spyOn($scope.editForm, '$setPristine');
        spyOn($scope.editForm, '$setUntouched');

        $scope.projectFeed = {title: 'something wonderful', id: null};
        $scope.trackerFeed = {projectId: '442903', id: null, projectFeed: $scope.projectFeed};
        $scope.gitHubFeed = {id: null, repositoryURL: null, repositoryOwner: 'johnlithgow', repositoryName: 'harryandthehendersons', projectFeed: $scope.projectFeed};
        $scope.save();

        httpMock.flush();

        expect($scope.editForm.$setPristine).toHaveBeenCalled();
        expect($scope.editForm.$setUntouched).toHaveBeenCalled();
    });
});
