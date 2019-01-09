/**
 * Created by kivi on 18.12.17.
 */

function ReportService($resource) {
    return $resource('rest/report/:id', {id: '@id'});
}

function ReportPermService($resource) {
    return $resource('rest/report/:id/permissions?user=:login', {id: '@id', login: '@login'});
}

function ReportProjectService($resource) {
    return $resource('rest/report/:id/project', {id: '@id'});
}

function ReportCommentService($resource) {
    return $resource('rest/report/:id/comments?user=:login', {id: '@id', login: '@login'});
}

function ReportController($scope, $http, $routeParams,
                          ReportService,
                          ReportPermService,
                          ReportCommentService,
                          ReportProjectService,
                          InfoShareService) {
    function url() {
        return {id: $routeParams.id};
    }
    function url_with_login(login) {
        return {id: $routeParams.id, login:login};
    }

    this.user = InfoShareService.getUser();
    this.permissions = ReportPermService.get(url_with_login(this.user.login));
    this.project = ReportProjectService.get(url());
    this.instance = ReportService.get(url());
    this.comments = ReportCommentService.query(url());

    this.changeStatus = function (status) {
        $http.put('rest/report/' + this.instance.id + '?user=' + this.user.login, status)
            .then(function () {
                this.update();
                this.updateComments();
            }.bind(this), function (error) {
                alert(error.data.message);
        });
    };

    this.commentReport = function () {
        if (isEmpty($scope.reportComment)) {
            alert("Enter comment message");
        } else {
            var comment = new ReportCommentService();
            comment.description = $scope.reportComment;
            comment.$save(url_with_login(this.user.login), function () {
                $scope.reportComment = "";
                this.updateComments();
            }.bind(this), function (error) {
                alert(error.data.message);
            });
        }
    };

    this.update = function () {
        this.instance = ReportService.get(url());
    };

    this.updateComments = function () {
        this.comments = ReportCommentService.query(url());
    }
}

app
    .factory('ReportService', ReportService)
    .factory('ReportPermService', ReportPermService)
    .factory('ReportProjectService', ReportProjectService)
    .factory('ReportCommentService', ReportCommentService)
    .controller('ReportController', ReportController);