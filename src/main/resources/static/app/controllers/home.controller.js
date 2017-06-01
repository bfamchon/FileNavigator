(function () {
    'use strict';

    angular
        .module('home', [])
        .controller('homeController', homeController)
        .directive('fileModel', function ($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                let model = $parse(attrs.fileModel);
                let modelSetter = model.assign;

                element.bind('change', function(){
                    scope.$apply(function(){
                        modelSetter(scope.homeCtrl, element[0].files[0]);
                    });
                });
            }
        }});

    homeController.$inject = ["homeService", "$window", "$scope"];

    function homeController(homeService, $window) {
        let vm = this;
        vm.adress = $window.location.origin;
        vm.data = homeService.getData();
        vm.newDirectoryName = "";
        vm.baseDir = {
            "type": "directory",
            "path": "/",
            "name": "users_workspace",
            "parent": "Welcome to my file manager !"
        };
        vm.newResourceName = "";

        vm.getContent = function (resource) {
            homeService.getContent(resource).then(function (response) {
                vm.data.dirContent = response[0].dirContent;
                vm.data.parent = response[1];
            });
        };

        vm.editContent = function (resource) {
            if (vm.newResourceName !== "" && vm.newResourceName !== resource.name) {
                vm.newResourceName = vm.newResourceName.replace(/(\.[^.]*)$/,'');
                resource.name = resource.name.replace(/^(.[^.]*)/, vm.newResourceName);
                homeService.editContent(resource,vm.newResourceName).then(() => {
                    resource.path = resource.path.replace(/([^\/]*)$/,resource.name);
                });
            }
        };


        vm.createDirectory = function () {
            homeService.createDirectory(vm.newDirectoryName, vm.data.parent);
        };

        vm.uploadFile = function () {
            homeService.uploadFile(vm.myFile, vm.data.parent);
        };

        vm.deleteContent = function (resource) {
            homeService.deleteContent(resource);
        };

        vm.getContent(vm.baseDir);
    }
})();