var app = angular.module('app', ['flow'])
    .config(['flowFactoryProvider', function (flowFactoryProvider) {
      flowFactoryProvider.defaults = {
        target: '/upload',
        permanentErrors:[404, 500, 501]
      };
      // You can also set default events:
      flowFactoryProvider.on('catchAll', function (event) {
      });
      // Can be used with different implementations of Flow.js
      // flowFactoryProvider.factory = fustyFlowFactory;
    }]);

app.controller('SampleController', [function () {
  //The body of demo controller
}]);

