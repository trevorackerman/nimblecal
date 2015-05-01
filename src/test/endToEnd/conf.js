var HtmlReporter = require('protractor-html-screenshot-reporter');

exports.config = {
    specs: ['trackerApiTokenSpec.js'],
    capabilities: {
        browserName: 'phantomjs',
        version: '',
        platform: 'ANY'
    },
    seleniumPort: 4444,
    onPrepare: function() {
        jasmine.getEnv().addReporter(new HtmlReporter({
            baseDirectory: 'build/test-results/endToEnd'
        }));
    }
};
