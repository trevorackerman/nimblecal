'use strict';

describe('Main Controller ', function () {
    var controller, $scope, principalSpy, deferred, spyPromise, httpMock, editFormSpy, calendarSpy;

    beforeEach(module('nimblecalApp'));

    beforeEach(inject(function ($rootScope, $controller, $q, $httpBackend, uiCalendarConfig) {

        calendarSpy = {
            fullCalendar: function(options) {
                console.log("spy was called");
            }
        };

        spyOn(calendarSpy, 'fullCalendar');

        uiCalendarConfig.calendars['projectCalendar'] = calendarSpy;

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
            { title: 'something wonderful', id: 1,
                trackerFeeds: [ {id : 1, projectId: 23452345} ],
                gitHubFeeds: [ {id: 1, repositoryOwner: "johnwayne", repsitoryName: "rioLobo", repositoryURL: "https://www.example.com/johnwayne/riolobo"}]
            }
        ]);
        otherStuff();
        httpMock.expectGET(/api\/trackerFeeds\/1\/events\?cacheBuster=.*/).respond(200, [
            { title: 'blah', start: '2015-06-19T15:50:00Z', end: '2015-06-19T17:50:00Z', allDay: false}
        ]);

        httpMock.expectGET(/api\/gitHubFeeds\/1\/events\?cacheBuster=.*/).respond(200, [
            { title: 'something', description: 'a funny thing happened...', start: '2015-08-11T21:50:00Z', end: '2015-08-11T21:50:00Z', allDay: false}
        ]);

        $scope.$apply();
        httpMock.flush();
        httpMock.verifyNoOutstandingRequest();
        expect($scope.projectFeeds.length).toBe(1);

        expect(calendarSpy.fullCalendar).toHaveBeenCalledWith('removeEventSource', [[]]);
        expect(calendarSpy.fullCalendar).toHaveBeenCalledWith('addEventSource', $scope.events);
        expect(calendarSpy.fullCalendar).toHaveBeenCalledWith('refetchEvents');

        expect(calendarSpy.fullCalendar).toHaveBeenCalledWith('removeEventSource', [[]]);
        expect(calendarSpy.fullCalendar).toHaveBeenCalledWith('addEventSource', $scope.gitHubEvents);
        expect(calendarSpy.fullCalendar).toHaveBeenCalledWith('refetchEvents');
    });
});
