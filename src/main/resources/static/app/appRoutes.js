angular.module('appRoutes', [])
    .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/', {
                title: 'Welcome to my file manager.',
                templateUrl: 'app/views/home.html',
                controller: 'homeController',
                controllerAs: 'homeCtrl'
            })
            .when('/about', {
                title: 'About this TP.',
                templateUrl: 'app/views/about.html',
                controller: 'homeController',
                controllerAs: 'homeCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });

        $locationProvider.html5Mode(true);

    }]);