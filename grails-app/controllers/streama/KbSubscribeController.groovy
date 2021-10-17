package streama


import grails.rest.*
import grails.converters.*
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import org.killbill.billing.client.model.gen.Account;
import org.killbill.billing.client.model.gen.Subscription;
import org.killbill.billing.client.model.gen.BlockingState;
import org.killbill.billing.entitlement.api.Entitlement.EntitlementState;

import groovy.util.logging.Slf4j;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import org.joda.time.LocalDate;

@Slf4j
class KbSubscribeController {
	static responseFormats = ['json', 'xml']
  def springSecurityService
  def kbSubscriptionService

    def index() { }

def subscribeUser(){


   def planName = params.planName;

    /*if(!planName){
        log.info("planName not found what the fuck is going on");
        render status: NOT_FOUND
        return
    } */
    log.info(String.format("subscribe user planName is %s", planName));
    User user = springSecurityService.currentUser
    planName = "wiflix-fixed-daily"

     //"wiflix-fixed-daily";


    if(kbSubscriptionService.userGotCashForPlan(user,planName)){//user has cash
      LocalDate requestDtTime = LocalDate.now();

      def subscribed =kbSubscriptionService.subscribeUserToPlan(user,planName,"Wiflix Movies");
    }else{
      def result = [:]
      result.message ="you don't have enough cash to subscribe to this plan please load your account"
      response.setStatus(NOT_ACCEPTABLE.value)
      respond result
      return
    }
}
def subscribeUser1(){


   def planName = params.planName;

    /*if(!planName){
        log.info("planName not found what the fuck is going on");
        render status: NOT_FOUND
        return
    } */
    log.info(String.format("subscribe user planName is %s", planName));
    User user = springSecurityService.currentUser
    planName = "wiflix-fixed-daily"

     //"wiflix-fixed-daily";
      def subscribed =kbSubscriptionService.subscribeUserToPlan(user,planName,"Wiflix Movies");
      def result = [:]
      result.message ="subscription create to "+ planName + "make sure you've made youre payment to start watching"
      respond result, [status: OK]
      return

}
def subscribeUser2(){


   def planName = params.planName;

    /*if(!planName){
        log.info("planName not found what the fuck is going on");
        render status: NOT_FOUND
        return
    } */
    log.info(String.format("subscribe user planName is %s", planName));
    User user = springSecurityService.currentUser
    planName = "wiflix-fixed-daily"

     //"wiflix-fixed-daily";


    if(kbSubscriptionService.userGotCashForPlan(user,planName)){//user has cash
        def subscribed =kbSubscriptionService.subscribeUserToPlan(user,planName,"Wiflix Movies");

        int noOfDays = kbSubscriptionService.retNoOfDays(planName);
    }else{
      def result = [:]
      result.message ="you don't have enough cash to subscribe to this plan please load your account"
      response.setStatus(NOT_ACCEPTABLE.value)
      respond result
      return
    }

}
def getUserSubscription(){
    User user = springSecurityService.currentUser
    Subscription subscription = kbSubscriptionService.getUserSubscriptions(user);

    if(subscription?.getSubscriptionId()){
        def subscript = [:];
        subscript.accountId = subscription.getAccountId();
        subscript.subscriptionId = subscription.getSubscriptionId();
        subscript.externalKey = subscription.getExternalKey();
        subscript.productName = subscription.getProductName();
        subscript.phaseType = subscription.getPhaseType();
        subscript.planName = subscription.getPlanName();
        subscript.state = subscription.getState();
        subscript.startDate   = subscription.getStartDate().toString();
        subscript.cancelledDate = subscription.getCancelledDate().toString();
        subscript.billingStartDate = subscription.getBillingStartDate().toString();
        subscript.billingEndDate = subscription.getBillingEndDate().toString();
        session['subscriptionId'] = subscription.getSubscriptionId();
        respond subscript;
        return
      }else{
        def result = [:]
        result.message ="unable to get user subscription for you please try again later "
        response.setStatus(NOT_ACCEPTABLE.value)
        respond result
        return
      }
}
def checkUserEntitlement(){
    User user = springSecurityService.currentUser
    def isEntitled = kbSubscriptionService.isUserEntitledToPlan(user);
}
def cancelUserSubscription(String planName){
      User user = springSecurityService.currentUser;
      def unsubscribed = kbSubscriptionService.cancelSubscriptionPlan(user, "wiflix-fixed-daily");
      if(unsubscribed?.getSubscriptionId()){
        respond getUserSubscription();
        return;
      }else{ //TODO:we must find a way to unsubscribe user notify admin
        def result = [:]
        result.message ="unable to get user subscription for you please try again later "
        response.setStatus(NOT_ACCEPTABLE.value)
        respond result
        return
      }
}

}
