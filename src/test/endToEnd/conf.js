var HtmlReporter = require('protractor-html-screenshot-reporter');

exports.config = {
    seleniumAddress: 'http://localhost:4444/wd/hub',
    specs: ['trackerApiTokenSpec.js'],
    capabilities: {
        browserName: 'phantomjs',
        version: '',
        platform: 'ANY'
    },

    onPrepare: function() {
        jasmine.getEnv().addReporter(new HtmlReporter({
            baseDirectory: 'build/test-results/e2e'
        }));
    }
};
