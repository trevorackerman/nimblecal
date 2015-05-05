describe('Tracker API Token Page', function() {
    beforeEach(function() {
        browser.driver.manage().window().setSize(1600, 1200);
        browser.driver.manage().window().maximize();

        browser.get('#/login');

        element(by.model('username')).sendKeys('admin');
        element(by.model('password')).sendKeys('admin');

        $('button.btn-primary').click();
        element(by.linkText('Account')).click();
        element(by.xpath('//a[@href = "#/trackerApiToken"]')).click();
    });

    // Wanted to have this be an afterEach() function but the screenshot reporter doesn't do its
    // work until afterEach() has run, which yields screenshots of the home page and logged out.
    function logout() {
        element(by.linkText('Account')).click();
        element(by.xpath('//a[@ng-click = "logout()"]')).click();
    }

    it('should not indicate that the token can be changed when a valid token has not been entered', function() {
        expect($('#changeTrackerApiToken').isDisplayed()).toBeFalsy();
        logout();
    });

    it('should instruct user to check api token when invalid', function() {
        element(by.model('token')).sendKeys('probablynotavalidtoken');
        $('button.btn-primary').click();
        expect($('div.alert-success').isDisplayed()).toBeFalsy();
        expect($('div.alert-danger').getText()).toEqual('Failed to validate API Token.\nCheck your Tracker API token.');
        logout();
    });

    it('should indicate when token is valid', function() {
        element(by.model('token')).sendKeys(browser.params.trackerApiToken);
        $('button.btn-primary').click();
        expect($('div.alert-success').getText()).toEqual('Valid Token.');
        expect($('div.alert-danger').isDisplayed()).toBeFalsy();
        logout();
    });

    it('should indicate that the token can be changed when a valid one has already been entered', function() {
        element(by.model('token')).sendKeys(browser.params.trackerApiToken);
        $('button.btn-primary').click();
        expect($('div.alert-success').getText()).toEqual('Valid Token.');
        expect($('div.alert-danger').isDisplayed()).toBeFalsy();

        browser.get('#/');
        element(by.linkText('Account')).click();
        element(by.xpath('//a[@href = "#/trackerApiToken"]')).click();

        expect($('#changeTrackerApiToken').getText()).toEqual('Change Current Tracker API Token');
        logout();
    });
});
