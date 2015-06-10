
describe('Home Page', function() {

    beforeEach(function() {
        browser.driver.manage().window().setSize(1600, 1200);
        browser.driver.manage().window().maximize();
        browser.get('/');
        browser.sleep(10);
    });

    it('Does not show the guest a calendar', function () {
        expect($('div.alert-warning').getText()).toContain("You don't have an account yet?");
        expect(element.all(by.xpath('//div[@ui-calendar]')).count()).toBe(0);
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
});
