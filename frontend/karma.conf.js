module.exports = function (config) {
    config.set({
        frameworks: ['jasmine'],

        browsers: ['ChromeHeadless'],

        coverageReporter: {
            dir: require('path').join(__dirname, './coverage/frontend'),
            subdir: '.',
            reporters: [
                { type: 'html' },
                { type: 'text-summary' },
                { type: 'lcovonly' }
            ]
        }
    });
};