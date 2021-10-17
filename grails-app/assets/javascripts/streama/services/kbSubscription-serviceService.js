'use strict';

angular
    .module("streama")
    .factory("kbSubscriptionService",function ($rootScope,apiService, $interval, $filter, contextPath, $translate) {
        var subscriptionData = null;
        $rootScope.subscriptionOptions = {};
        $rootScope.userAccount = {};
        var billEndDate = new Date();
        billEndDate.setDate(billEndDate.getDate()+1);

        var defaultSubscription = {
            accountId: '',
            subscriptionId: '',
            externalKey: '',
            startDate: new Date(),
            productName: 'wiflixMovies',
            phaseType: 'TRIAL',
            planName: 'wiflixDaily',
            state: "",
            cancelledDate: '',
            billingStartDate: new Date(),
            billingEndDate: billEndDate,
        };
        var defaultAccount = {
            accountId:'',
            accountName: '',
            balance: 0.0
        };
      return {
      saveSubscription: function(data) {
        $rootScope.subscriptionOptions = data;
    },
    setUserSubscription: function(serverSubs){
        $rootScope.subscriptionOptions = angular.copy(defaultSubscription);
        $rootScope.subscriptionOptions.accountId = serverSubs.accountId;
        $rootScope.subscriptionOptions.subscriptiontId = serverSubs.subscriptionId;
        $rootScope.subscriptionOptions.externalKey = serverSubs.externalKey;
        $rootScope.subscriptionOptions.startDate = serverSubs.startDate;
        $rootScope.subscriptionOptions.productName = serverSubs.productName;
        $rootScope.subscriptionOptions.phaseType = serverSubs.phaseType;
        $rootScope.subscriptionOptions.planName = serverSubs.planName;
        $rootScope.subscriptionOptions.state = serverSubs.state;
        $rootScope.subscriptionOptions.cancelledDate = serverSubs.cancelledDate;
        $rootScope.subscriptionOptions.billingStartDate = serverSubs.billingStartDate;
        $rootScope.subscriptionOptions.billingEndDate   = serverSubs.billingEndDate;
        console.log($rootScope.subscriptionOptions);

    },
    getUserSubscription: function(){
        var that = this;
        apiService.subscription.getUserSubscription().then(function(response){
            that.setUserSubscription(response.data);


        }, function (data) {
            console.log(data);
            alertify.error(data.message);
        });

    },
    isUserEntitledToService: function(){
        if(!$rootScope.subscriptionOptions){
            this.getUserSubscription();
        }
        if($rootScope.subscriptionOptions.state === 'ACTIVE'){
            console.log("is entitled returned true");
            return true;
        }else{
            console.log("is entitled returned false");
            return false;
        }
    },
    setUserAccount: function (accountdata) {
        $rootScope.userAccount = angular.copy(defaultAccount);
        $rootScope.userAccount.accountName = accountdata.name;
        $rootScope.userAccount.accountId = accountdata.accountId;
        $rootScope.userAccount.balance = accountdata.balance;
        console.log($rootScope.userAccount);
    },
    getUserAccount: function () {
        var that = this;
        apiService.account.getUserAccount().then(function (response) {
        that.setUserAccount(response.data);
        }, function (data) {
              console.log(data);
              alertify.error(data.message);
        });
    },
    userMullaForService: function () {
        if (!$rootScope.userAccount) {
            this.getUserAccount();
        }
        console.log($rootScope.userAccount.balance);
        if ($rootScope.userAccount.balance >= 50.0) {
            console.log("user is entitled to watch");
            return true;
        } else {
            console.log("user not entitled to watch");
            return false;
        }
    }

  };
});
