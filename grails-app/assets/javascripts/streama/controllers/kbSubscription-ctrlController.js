'use strict';

angular
    .module("streama")
    .controller("KbSubscriptionCtrl",['$scope', 'apiService','kbSubscriptionService' ,'$state', '$rootScope',function ($scope,apiService,kbSubscriptionService,$state,$rootScope) {
      $scope.subscription =[
      {planName: "Wiflix-fixed-daily",productName:'wiflix', amount: 30.0, duration: "24hrs", features: ["hd movies","unlimited movies"]},
      {planName: "WiflixWeekly1" ,productName:'wiflix3', amount: 150.0, duration: "1week", features: ["2k movies","unlimited movies"]},
      {planName: "WiflixMonthly",productName:'wiflix2', amount: 500.0, duration: "1month", features: ["watch upto 4k movies","unlimited movies"]},
      ];
      $scope.loading = false;
      $scope.createUserSubscription = function(planName){
          //subscribe user to a plan
          $scope.loading = true;
          if (planName == "wiflix-fixed-daily") {
          apiService.subscription.createUserSubscription(planName).then(function(results){
              console.info(results); //TODO: remove this later
              kbSubscriptionService.getUserSubscription();
              alertify.success(" You have subscribed to "+ planName);
              $scope.loading = false;
              //redirect user to the movie he/she was trying to watch //TODO: remove this comments after this
              //TODO: but first is user entitled to watch this movie at the moment


              $state.go('dash', {});//TODO: show user success
            },function(error){//TODO: I have a error message div write something there
                console.error(error);
                alertify.error(error.message);
                $scope.loading = false;
                //TODO: what should we do on error
                $state.go('dash', {});
            });
          }if(planName === "wiflixWeekly1"){
            apiService.subscription.createUserSubscription1(planName).then(function(results){
              console.info(results); //TODO: remove this later
              kbSubscriptionService.getUserSubscription();
              alertify.success(" You have subscribed to "+ planName);
              $scope.loading = false;
              //redirect user to the movie he/she was trying to watch //TODO: remove this comments after this
              //TODO: but first is user entitled to watch this movie at the moment

              $state.go('dash', {});//TODO: show user success
            },function(error){//TODO: I have a error message div write something there
                console.error(error);
                alertify.error(error.message);
                $scope.loading = false;
                //TODO: what should we do on error
                $state.go('dash', {});
            });
          }
            apiService.subscription.createUserSubscription2(planName).then(function(results){
                        console.info(results); //TODO: remove this later
                        kbSubscriptionService.getUserSubscription();
                        alertify.success(" You have subscribed to "+ planName);
                        $scope.loading = false;
                        //redirect user to the movie he/she was trying to watch //TODO: remove this comments after this
                        //TODO: but first is user entitled to watch this movie at the moment

                        $state.go('dash', {});//TODO: show user success
                        },function(error){//TODO: I have a error message div write something there
                            console.error(error);
                            alertify.error(error.message);
                            $scope.loading = false;
                            //TODO: what should we do on error
                            $state.go('dash', {});
            });
      };
      $scope.setUp = function(){
      //setup all the plans that we have for client
      };
}]);
