
describe('Home Page', function() {

    beforeEach(function() {
        browser.driver.manage().window().setSize(1600, 1200);
        browser.driver.manage().window().maximize();
        browser.get('/');
        browser.sleep(10);
    });

    it('Shows the guest the welcome panel and a sample calendar', function () {
        expect($('div.goat-bg').isDisplayed).toBeTruthy();
        expect($('#example').isDisplayed()).toBeFalsy();
        $('#exampleToggle').click();
        expect($('div.goat-bg').isDisplayed()).toBeFalsy();
        expect($('#example').isDisplayed()).toBeTruthy();
        expect(element.all(by.css('div.fc-content-skeleton table thead tr td.fc-day-number ')).count()).toBeGreaterThan(27);
    });


    it('Shows an authenticated user a calendar', function() {
        browser.get('#/login');

        element(by.model('username')).sendKeys('user');
        element(by.model('password')).sendKeys('user');

        $('button.btn-primary').click();
        browser.sleep(10);

        expect($('div.alert-success').getText()).toContain('You are logged in as user "user"');
        expect(element.all(by.xpath('//div[@ui-calendar]')).count()).toBe(1);
    });

    it('alerts user when they have no project feeds', function() {
        browser.get('#/login');

        element(by.model('username')).sendKeys('noprojects');
        element(by.model('password')).sendKeys('user');

        $('button.btn-primary').click();
        browser.sleep(10);

        expect($('div.alert-warning').getText()).toContain("You don't have any project feeds yet");
        expect($('div.btn-group ul.dropdown-menu').isDisplayed()).toBeFalsy();
    });

    it('shows user dropdown to choose project feed on calendar', function() {
        browser.get('#/login');

        element(by.model('username')).sendKeys('multipleprojects');
        element(by.model('password')).sendKeys('user');

        $('button.btn-primary').click();
        browser.sleep(250);

        expect($('div.alert-warning').isDisplayed()).toBeFalsy();
        var ddlist = element.all(by.css('div.btn-group ul.dropdown-menu li'));
        expect(ddlist.count()).toBe(3);
    });

    it('allows the user to store their Tracker API token in session', function() {
        browser.get('#/login');

        element(by.model('username')).sendKeys('noprojects');
        element(by.model('password')).sendKeys('user');

        $('button.btn-primary').click();
        browser.sleep(10);

        expect($('div.alert-danger').getText()).toContain("You need to enter your Tracker API token");
        element(by.model('trackerApiToken')).sendKeys('notrealtoken');
        expect($('div.alert-danger').isDisplayed()).toBeFalsy();

        $('button.btn-default').click();
        browser.get('#/settings');
        browser.get('#/');
        expect($('div.alert-danger').isDisplayed()).toBeFalsy();

    });
});
