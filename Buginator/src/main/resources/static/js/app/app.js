angular.module("buginator", [
    "ngResource",
    "smoothScroll",
    "ui.router",
    "ui.bootstrap",
    "ngDialog",
    "smart-table",

    "buginator.directives",
    "buginator.filters",

    "buginator.authService",
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