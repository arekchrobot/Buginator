angular.module("buginator", [
    "ngResource",
    "smoothScroll",
    "ui.router",
    "ui.bootstrap",
    "ngDialog",
    "smart-table",
    "LocalStorageModule",

    "buginator.directives",
    "buginator.filters",

    "buginator.exceptionHandler",
    "buginator.dialogController",
    "buginator.baseRestService",
    "buginator.cacheService",

    "buginator.authService",
    "buginator.registerService",
    "buginator.authController"

]).config(["$sceDelegateProvider", "$httpProvider", "$urlRouterProvider", function ($sceDelegateProvider, $httpProvider, $urlRouterProvider) {

    //$urlRouterProvider.when("/", "login").otherwise("/");
    delete $httpProvider.defaults.headers.common['X-Requested-With'];

    $sceDelegateProvider
        .resourceUrlWhitelist([
            "self",
            "https://www.youtube.com/embed/**"
        ]);
}]);