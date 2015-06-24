var fs = require('fs');


describe('Project Feeds Page', function() {
    var originalProjectCount = 0;
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

        var time = new Date().getTime();

        $('div.row button.btn-primary').click();
        browser.sleep(1000);
        element(by.model('projectFeed.title')).sendKeys('Concord-' + time);
        element(by.model('trackerFeed.projectId')).sendKeys('442903-' + time);
        browser.sleep(10);
        element(by.model('githubFeed.repositoryURL')).sendKeys("https://example.com/githubrepo");

        $('form button.btn-primary').click();
        browser.sleep(250);

        $$('table tbody tr').count().then(function(currentCount) {
            expect(currentCount).toEqual(originalProjectCount + 1);
            expect($$('table tbody tr').getText()).toContain("Concord-" + time + "\n442903-" + time);
            //expect(projectRow.$$('td').get(2).getText()).toEqual('https://github.com/cloudfoundry/loggregator');
        });

    });

    it('should allow the user to delete a project feed', function() {

    });
});

