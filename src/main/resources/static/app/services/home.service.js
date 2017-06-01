(function () {
    'use strict';

    angular
        .module('home')
        .factory('homeService', homeService);
    homeService.$inject = ["$http"];

    function homeService($http) {
        let sm = this;
        sm.data = {
            "dirContent": [],
            "parent": {}
        };

        function getData() {
            return sm.data;
        }

        /**
         * Call our getters in function of resource's type
         * @param resource object with params { type: 'directory' or 'file', path: 'something/other/thing' }
         */
        function getContent(resource) {
            return $http.get(resource.type + resource.path + "/").then(function (resources) {
                sm.data.dirContent = resources.data[0].dirContent;
                sm.data.parent = resources.data[1];
                return resources.data;
            });
        }

        function deleteContent(resource) {
            $http.delete(resource.type + resource.path + "/").then(function (response) {
                for (let i = 0; i < sm.data.dirContent.length; ++i) {
                    if (sm.data.dirContent[i].name === response.data.name) {
                        sm.data.dirContent.splice(i, 1);
                    }
                }
            });
        }

        function createDirectory(name, parent) {
            let currentDir = parent.path;
            currentDir = currentDir.replace(/\.\.\/$/, '');
            $http({
                url: "directory" + currentDir,
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                params: {
                    name: name
                }
            }).then(function (response) {
                sm.data.dirContent.push(response.data);
            });
        }

        function uploadFile(file, parent) {
            let fd = new FormData();
            fd.append('file', file);
            let currentDir = parent.path;
            currentDir = currentDir.replace(/\.\.\/$/, '');
            $http.post("file" + currentDir, fd,{
                headers: { 'Content-Type': undefined },
                transformRequest: angular.identity
            }).then(function (response) {
                sm.data.dirContent.push(response.data);
            });
        }

        function editContent(resource, name) {
            return $http.put(resource.type + resource.path,{},
                {params: {
                    name: name
                }
            })
        }

        return {
            getContent: getContent,
            deleteContent: deleteContent,
            getData: getData,
            createDirectory: createDirectory,
            uploadFile: uploadFile,
            editContent: editContent
        }

    }
})();