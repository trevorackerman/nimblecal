var fs = require('fs');


describe('Project Feeds Page', function() {
    var originalProjectCount = 0;
    var time = new Date().getTime();

    beforeEach(function() {
        browser.driver.manage().window().setSize(1600, 1200);
        browser.driver.manage().window().maximize();

        browser.get('#/login');

        element(by.model('username')).sendKeys('user');
        element(by.model('password')).sendKeys('user');

        $('button.btn-primary').click();
        element(by.linkText('Project Feeds')).click();

        $$('table tbody tr').count().then(function(originalCount) {
            originalProjectCount = originalCount;
        });
    });

    // Wanted to have this be an afterEach() function but the screenshot reporter doesn't do its
    // work until afterEach() has run, which yields screenshots of the home page and logged out.
    function logout() {
        element(by.linkText('Account')).click();
        element(by.xpath('//a[@ng-click = "logout()"]')).click();
    }

    it('should allow the user to create a project feed', function() {
        $('div.row button.btn-primary').click();
        browser.sleep(1000);
        element(by.model('projectFeed.title')).sendKeys('Concord-' + time);
        element(by.model('trackerFeed.projectId')).sendKeys('442903-' + time);
        browser.sleep(10);
        element(by.model('gitHubFeed.repositoryOwner')).sendKeys("theduke");
        element(by.model('gitHubFeed.repositoryName')).sendKeys("truegrit");

        $('form button.btn-primary').click().then(function() {
            browser.sleep(1000);

            $$('table tbody tr').count().then(function(currentCount) {
                expect(currentCount).toEqual(originalProjectCount + 1);
                expect($$('table tbody tr').getText()).toContain("Concord-" + time + "\n442903-" + time +"\nhttps://github.com/theduke/truegrit\nDelete");
            });
        });
    });

    it('should allow the user to cancel deleting a project feed', function() {
        element.all(by.repeater('projectFeed in projectFeeds')).then(function(projectFeeds) {
            var count = projectFeeds.length;

            expect(projectFeeds[0].$('td button.btn-danger').getText()).toEqual('Delete');

            projectFeeds[0].element(by.css('button.btn-danger')).click().then(function () {
                browser.sleep(200);

                $('#deleteProjectFeedsModal button.btn-default').click().then(function () {
                    browser.sleep(200);
                    expect(element(by.id('deleteProjectFeedsModal')).isDisplayed()).toBeFalsy();
                    element.all(by.repeater('projectFeed in projectFeeds')).then(function (projectFeeds) {
                        expect(projectFeeds.length).toBe(count);
                    });
                });
            });
        });
    });

    it('should allow the user to delete a project feed', function() {
        element.all(by.repeater('projectFeed in projectFeeds')).then(function(projectFeeds) {
            var count = projectFeeds.length;

            projectFeeds[0].element(by.css('button.btn-danger')).click().then(function () {
                browser.sleep(200);

                $('#deleteProjectFeedsModal button.btn-danger').click().then(function () {
                    browser.sleep(200);
                    expect(element(by.id('deleteProjectFeedsModal')).isDisplayed()).toBeFalsy();
                    element.all(by.repeater('projectFeed in projectFeeds')).then(function (projectFeeds) {
                        expect(projectFeeds.length).toBe(count - 1);
                    });
                });
            });
        });
    });
});

