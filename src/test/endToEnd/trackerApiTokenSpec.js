describe('Tracker API Token Page', function() {
    it('should instruct user to check api token when it is not valid and indicate when it is valid', function() {
        browser.driver.manage().window().setSize(1600, 1200);
        browser.driver.manage().window().maximize();

        browser.get('#/login');

        element(by.model('username')).sendKeys('admin');
        element(by.model('password')).sendKeys('admin');

        $('button.btn-primary').click();

        element(by.linkText('Account')).click();
        element(by.xpath('//a[@href = "#/trackerApiToken"]')).click();

        expect($('div.alert-success').isDisplayed()).toBeFalsy();
        expect($('div.alert-danger').isDisplayed()).toBeFalsy();

        element(by.model('token')).sendKeys(browser.params.trackerApiToken);
        $('button.btn-primary').click();
        browser.waitForAngular().then(function () {
            expect($('div.alert-success').getText()).toEqual('Valid Token.');
            expect($('div.alert-danger').isDisplayed()).toBeFalsy();
        });

        element(by.model('token')).sendKeys('probablynotavalidtoken');
        $('button.btn-primary').click();
        browser.waitForAngular().then(function () {
            expect($('div.alert-success').isDisplayed()).toBeFalsy();
            expect($('div.alert-danger').getText()).toEqual('Failed to validate API Token.\nCheck your Tracker API token.');
        });


    });
});
