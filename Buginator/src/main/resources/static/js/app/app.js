angular.module("buginator", [
    "ngResource",
    "smoothScroll",
    "ui.router",
    "ui.bootstrap",
    "ngDialog",
    "smart-table",
    "LocalStorageModule",
    "pascalprecht.translate",
    "ngCookies",
    "ngSanitize",
    "chart.js",
    "ngIdle",

    "buginator.directives",
    "buginator.filters",

    "buginator.exceptionHandler",
    "buginator.dialogController",
    "buginator.baseRestService",
    "buginator.cacheService",

    "buginator.authService",
    "buginator.registerService",
    "buginator.authController",

    "buginator.dashboardController",

    "buginator.applicationServices",
    "buginator.applicationsController",
    "buginator.createApplicationController",
    "buginator.detailsApplicationController",

    "buginator.chartServices"

]).config(["$sceDelegateProvider", "$httpProvider", "$urlRouterProvider", "$translateProvider", "IdleProvider", "KeepaliveProvider",
    function ($sceDelegateProvider, $httpProvider, $urlRouterProvider, $translateProvider, IdleProvider, KeepaliveProvider) {

        $urlRouterProvider
            .when("/signup", "login")
            .when("/forgot", "login");
        //.otherwise("/");
        delete $httpProvider.defaults.headers.common['X-Requested-With'];

        $sceDelegateProvider
            .resourceUrlWhitelist([
                "self"
            ]);

        $translateProvider.useLocalStorage();
        $translateProvider.useStaticFilesLoader({
            files: [{
                prefix: "js/app/languages/",
                suffix: ".json"
            }, {
                prefix: "js/app/languages/auth/",
                suffix: ".json"
            }, {
                prefix: "js/app/languages/application/",
                suffix: ".json"
            }]
        });
        $translateProvider.registerAvailableLanguageKeys(['en', 'pl'], {
            "en_*": "en",
            "pl_*": "pl",
            "*": "en"
        });
        $translateProvider.determinePreferredLanguage();
        $translateProvider.useSanitizeValueStrategy('sanitizeParameters');

        IdleProvider.idle(10 * 60); //10 minutes idle
        IdleProvider.timeout(30); //after 30 seconds idle, time the user out
        KeepaliveProvider.interval(5 * 60); // 5 minute keep-alive ping

    }])
    .run(function ($rootScope, $state, Idle) {
        Idle.watch();
        $rootScope.$on('IdleTimeout', function () {
            console.log("userTimedOut");
            $rootScope.user = null;
            $state.go("login");
        });
    });