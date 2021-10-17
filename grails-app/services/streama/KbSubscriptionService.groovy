package streama

import grails.transaction.Transactional;
import groovy.transform.InheritConstructors;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import java.math.BigDecimal;

import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.model.gen.Subscription;
import org.killbill.billing.client.model.gen.Account;
import org.killbill.billing.client.model.gen.BlockingState;
import org.killbill.billing.client.model.BlockingStates;
import org.killbill.billing.catalog.api.BillingActionPolicy;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.PhaseType;
import org.killbill.billing.catalog.api.PriceListSet;
import org.killbill.billing.catalog.api.ProductCategory;
import org.killbill.billing.entitlement.api.Entitlement.EntitlementActionPolicy;
import org.killbill.billing.entitlement.api.Entitlement.EntitlementState;
import org.killbill.billing.entitlement.api.SubscriptionEventType;
import org.killbill.billing.util.api.AuditLevel;

import java.util.UUID;
import groovy.util.logging.Slf4j;

@Slf4j
@Transactional
@InheritConstructors
class KbSubscriptionService extends KbClientService{
    def kbAccountService


    def serviceMethod() {
    }

    def getUserSubscriptions(User user){
        try{
            return subscriptionApi.getSubscriptionByKey(user.getUuid(),AuditLevel.NONE,requestOptions);
        }catch(KillBillClientException kbEx){
            log.error(kbEx.getMessage());
            return null;
        }
    }

    def isUserEntitledToPlan(User user){
        def userAcc = kbAccountService.getUserAccountByKey(user.getUsername());
        def subscrib = getUserSubscriptions(user);
        log.info(userAcc?.toString());
        if(subscrib?.getState() == EntitlementState.ACTIVE){
            return true;
        }
        return false;
    }

    def retNoOfDays(String planName){
        if( planName == "wiflixMonthly"){
            return 2;
        }else if(planName == "wiflixWeekly1"){
            return 6;
        }
        return 1;
    }

    def retOrgAmount(String planName){
        if(planName == "wiflix-fixed-daily"){
            return BigDecimal.valueOf(50.0);
        }else if(planName == "wiflixWeekly1"){
            return BigDecimal.valueOf(250.0);
        }
        return BigDecimal.valueOf(800.0);
    }

    def subscribeUserToPlan(User user, String planName, String productName){

      Account userAccount = kbAccountService.getUserAccountByKey(user.getUsername());

      Subscription body = new Subscription();
      LocalDate billingDate = LocalDate.now();
      LocalDate entitlementDate = null;


      Subscription olderSubscription = getUserSubscriptions(user);
      body.setAccountId(userAccount.getAccountId());
      body.setPlanName(planName);
      body.setExternalKey(user.getUuid());

      Boolean renameKeyIfExistsAndUnused = true;
      Boolean migrated = false;
      Integer bcd = null;
      Boolean callCompletion = true;
      long DEFAULT_WAIT_COMPLETION_TIMEOUT_SEC = 20;
      //ImmutableMap<String, String> NULL_PLUGIN_PROPERTIES = null;

      try{
          log.info("inside of subscription before anything.................");
          if(!olderSubscription?.getSubscriptionId()){
                log.info("creating subscription .......................");
                  olderSubscription = subscriptionApi.createSubscription(body,
                                                        entitlementDate,
                                                        billingDate,
                                                        renameKeyIfExistsAndUnused,
                                                        migrated,
                                                        callCompletion,
                                                        DEFAULT_WAIT_COMPLETION_TIMEOUT_SEC,
                                                        null,
                                                        requestOptions);
            }

            return  subscriptionApi.getSubscription(olderSubscription.getSubscriptionId(), requestOptions);
      }catch(KillBillClientException kbEx){
          log.error(kbEx.getMessage());
          return null;
      }
    }

    def cancelSubscriptionPlan(User username, String planName){

        try{
        Subscription olderSubscription = getUserSubscriptions(username); //TODO what if this returns > 1?
        LocalDate requestDate = LocalDate.now();
        log.info("..............cancelling Subscription...................");
        EntitlementActionPolicy entitlementPolicy =null;
        BillingActionPolicy billingPolicy =null;
        int noOfDays = retNoOfDays(planName);

        Boolean callCompletion = true;
        long DEFAULT_WAIT_COMPLETION_TIMEOUT_SEC = 20;

        subscriptionApi.cancelSubscriptionPlan(olderSubscription.getSubscriptionId(),
                                                                            requestDate.plusDays(noOfDays), //TODO: plusDays should be added from plan
                                                                            callCompletion,
                                                                            DEFAULT_WAIT_COMPLETION_TIMEOUT_SEC,
                                                                            entitlementPolicy,
                                                                            billingPolicy,
                                                                            true,
                                                                            null,
                                                                            requestOptions);


            return  subscriptionApi.getSubscription(olderSubscription.getSubscriptionId(), requestOptions);
        }catch(KillBillClientException kbEx){
            log.error(kbEx.getMessage());
            return null;
        }

    }
  def uncancelSubscriptionPlan(User username){
    try{
      Subscription olderSubscription = getUserSubscriptions(username);
      log.info("unCancelling cancelled subscription...............");
      subscriptionApi.uncancelSubscriptionPlan(olderSubscription.getSubscriptionId(),
                                                NULL_PLUGIN_PROPERTIES,
                                                requestOptions);


    }catch(KillBillClientException kbEx){
      log.error(kbEx.getMessage());
      return null;
    }
  }
def blockUserEntitlement(User user,Boolean blockEntitlement, Boolean blockBilling, String stateName, String serviceName, LocalDate requestDate){

      Account userAccount = kbAccountService.getUserAccountByKey(user.getUsername());

      BlockingState blockingState = new BlockingState();
      blockingState.setStateName(stateName);
      blockingState.setService(serviceName);
      blockingState.setIsBlockChange(false);
      blockingState.setIsBlockBilling(blockBilling);
      blockingState.setIsBlockEntitlement(blockEntitlement);
      try{
        log.info("just before addBlockingState func execution")
      BlockingStates result = accountApi.addAccountBlockingState(userAccount.getAccountId(),
                                                                blockingState,
                                                                requestDate,
                                                                NULL_PLUGIN_PROPERTIES,
                                                                requestOptions);
        log.info("after addBlocking state func execution")
      }catch(KillBillClientException kbEx){
            log.error(kbEx.getMessage());
            return null;
      }

  }
  def userGotCashForPlan(User user, String planName){
        Account userAccount = kbAccountService.getUserAccountByKey(user.getUsername());
        BigDecimal planAmount = retOrgAmount(planName);
        BigDecimal accountBal = userAccount.getAccountBalance().negate();
        if(accountBal.compareTo(planAmount) >= 0){
          return true;
        }
        return false;
  }

}
