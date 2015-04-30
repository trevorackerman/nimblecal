describe('Tracker API Token Page', function() {
    beforeEach(function() {
        browser.driver.manage().window().maximize();

        browser.get('#/login');

        element(by.model('username')).sendKeys('admin');
        element(by.model('password')).sendKeys('admin');

        $('button.btn-primary').click();

        element(by.linkText('Account')).click();
        element(by.xpath('//a[@href = "#/trackerApiToken"]')).click();
    });

    it('should instruct user to check api token when it is not valid', function() {
        element(by.model('token')).sendKeys('probablynotavalidtoken');
        $('button.btn-primary').click();

        expect($('div.alert-danger').getText()).toEqual('Failed to validate API Token.\nCheck your Tracker API token.');
    });

    it('should inform user when api token is valid', function() {
        element(by.model('token')).sendKeys(browser.params.trackerApiToken);
        $('button.btn-primary').click();

        expect($('div.alert-success').getText()).toEqual('Valid Token.');
    });
});
