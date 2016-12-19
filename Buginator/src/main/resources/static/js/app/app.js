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

    "buginator.applicationService",
    "buginator.applicationsController"

]).config(["$sceDelegateProvider", "$httpProvider", "$urlRouterProvider", "$translateProvider", function ($sceDelegateProvider, $httpProvider, $urlRouterProvider, $translateProvider) {

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
        }]
    });
    $translateProvider.registerAvailableLanguageKeys(['en', 'pl'], {
        "en_*": "en",
        "pl_*": "pl",
        "*":"en"
    });
    $translateProvider.determinePreferredLanguage();
    $translateProvider.useSanitizeValueStrategy('sanitizeParameters');

}]);