
function ProjectService($resource) {
    return $resource('rest/project/:name?user=:login', {name: '@name', login: '@login'});
}

function ProjectPermService($resource) {
    return $resource('rest/project/:name/permissions?user=:login', {name: '@name', login: '@login'});
}

function ProjectReportService($resource) {
    return $resource('rest/project/:name/reports', {name: '@name'});
}

function ProjectMilestoneService($resource) {
    return $resource('rest/project/:name/milestones?user=:login', {name: '@name', login:'@login'});
}

function ProjectDeveloperService($resource) {
    return $resource('rest/project/:name/developers?user=:login', {name: '@name', login: '@login'});
}

function ProjectTesterService($resource) {
    return $resource('rest/project/:name/testers?user=:login', {name: '@name', login: '@login'});
}

function ProjectController($scope, $routeParams, $http,
                           InfoShareService,
                           ProjectService,
                           ProjectPermService,
                           ProjectReportService,
                           ProjectMilestoneService,
                           ProjectDeveloperService,
                           ProjectTesterService) {
    var url = function () {
        return {name:$routeParams.projectName};
    };
    var url_with_user = function (login) {
        return {name:$routeParams.projectName, login: login};
    };

    this.user = InfoShareService.getUser();
    this.permissions = ProjectPermService.get(url_with_user(this.user.login));
    this.instance = ProjectService.get(url());
    this.reports = ProjectReportService.query(url());
    this.milestones = ProjectMilestoneService.query(url());
    this.developers = ProjectDeveloperService.query(url());
    this.testers = ProjectTesterService.query(url());

    this.setTeamLeader = function () {
        if (isEmpty($scope.teamLeaderLogin)) {
            alert("Enter new team leader login");
        } else {
            $http.put('rest/project/' + this.instance.name + '?user=' + this.user.login, $scope.teamLeaderLogin)
                .then(function () {
                    this.updateInstance();
                    this.updateDevelopers();
                }.bind(this), function (error) {
                    alert(error.data.message);
                });
        }
    };

    this.addDeveloper = function () {
        if (isEmpty($scope.developerLogin)) {
            alert("Enter new developer login");
        } else {
            var dev = new ProjectDeveloperService();
            dev.login = $scope.developerLogin;
            dev.$save(url_with_user(this.user.login), function () {
                $scope.developerLogin = "";
                this.updateDevelopers();
            }.bind(this), function (error) {
                alert(error.data.message);
            });
        }
    };

    this.addTester = function () {
        if (isEmpty($scope.testerLogin)) {
            alert("Enter new tester login");
        } else {
            var tester = new ProjectTesterService();
            tester.login = $scope.testerLogin;
            tester.$save(url_with_user(this.user.login), function () {
                $scope.testerLogin = "";
                this.updateTesters();
            }.bind(this), function (error) {
                alert(error.data.message);
            });
        }
    };

    this.addReport = function () {
        if (isEmpty($scope.reportDesc)) {
            alert("Empty report message");
        } else {
            var report = new ProjectReportService();
            report.creator = this.user;
            report.description = $scope.reportDesc;
            report.$save(url(), function () {
                $scope.reportDesc = "";
                this.updateReports();
            }.bind(this), function (error) {
                alert(error.data.message);
            });
        }
    };

    this.addMilestone = function () {
        var milestone = new ProjectMilestoneService();
        milestone.startingDate = $scope.startTime.getTime();
        milestone.endingDate = $scope.endTime.getTime();
        milestone.$save(url_with_user(this.user.login), function () {
            this.updateMilestones();
        }.bind(this), function (error) {
            alert(error.data.message);
        });
    };

    this.updateInstance = function () {
        this.instance = ProjectService.get(url());
    };

    this.updateReports = function () {
        this.reports = ProjectReportService.query(url());
    };

    this.updateMilestones = function () {
        this.milestones = ProjectMilestoneService.query(url());
    };

    this.updateDevelopers = function () {
        this.developers = ProjectDeveloperService.query(url());
    };

    this.updateTesters = function () {
        this.testers = ProjectTesterService.query(url());
    };
}

app.factory('ProjectService', ProjectService)
    .factory('ProjectPermService', ProjectPermService)
    .factory('ProjectReportService', ProjectReportService)
    .factory('ProjectMilestoneService', ProjectMilestoneService)
    .factory('ProjectDeveloperService', ProjectDeveloperService)
    .factory('ProjectTesterService', ProjectTesterService)
    .controller('ProjectController', ProjectController);