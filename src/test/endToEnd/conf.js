var HtmlReporter = require('protractor-html-screenshot-reporter');

exports.config = {
    specs: ['trackerApiTokenSpec.js'],
    capabilities: {
        browserName: 'phantomjs',
        'phantomjs.binary.path': 'node_modules/phantomjs/bin/phantomjs',
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
