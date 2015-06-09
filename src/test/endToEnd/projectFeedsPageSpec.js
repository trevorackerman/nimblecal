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
            console.log("#### " + originalProjectCount);
        });
    });

    // Wanted to have this be an afterEach() function but the screenshot reporter doesn't do its
    // work until afterEach() has run, which yields screenshots of the home page and logged out.
    function logout() {
        element(by.linkText('Account')).click();
        element(by.xpath('//a[@ng-click = "logout()"]')).click();
    }

    describe('whether or not a user has project feeds', function() {
        it('should allow the user to create a project feed', function() {

            $('div.row button.btn-primary').click();
            browser.sleep(250);
            element(by.model('projectFeed.title')).sendKeys('My Wonderful Project');
            element(by.model('trackerFeed.projectId')).sendKeys('993188');
            browser.sleep(10);
            element(by.model('githubFeed.repositoryURL')).sendKeys("https://github.com/cloudfoundry/loggregator");

            $('form button.btn-primary').click();
            browser.sleep(250);

            $$('table tbody tr').count().then(function(currentCount) {
                console.log("#### " + currentCount);
                expect(currentCount).toEqual(originalProjectCount + 1);
                var projectRow = $$('table tbody tr').get(originalProjectCount);

                expect(projectRow.$$('td').get(0).getText()).toEqual('My Wonderful Project');
                expect(projectRow.$$('td').get(1).getText()).toEqual('993188');
                //expect(projectRow.$$('td').get(2).getText()).toEqual('https://github.com/cloudfoundry/loggregator');
            });

        })
    });
});

