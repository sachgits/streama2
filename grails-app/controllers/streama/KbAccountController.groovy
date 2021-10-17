package streama


import grails.rest.*
import grails.converters.*
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.client.model.gen.Account;
import java.math.BigDecimal;
import groovy.util.logging.Slf4j;


@Slf4j
class KbAccountController {
	static responseFormats = ['json', 'xml']
  def springSecurityService

    def index() { }

    def getUserAccount(){
      User user = springSecurityService.currentUser
      Account userAcc = kbAccountService.getUserAccountByKey(user.getUsername());
      if(userAcc?.getAccountId()){
            BigDecimal accountBal = userAcc.getAccountBalance().negate();
            def account = [:];
            account.balance = accountBal.doubleValue();
            account.accountId= userAcc.getAccountId();
            session['accountId']= account.accountId;
            account.name =userAcc.getUsername();
            respond account;
            return
      }else{
          def result = [:]
          result.message ="unable to get user Account for payment please try again later "
          response.setStatus(NOT_ACCEPTABLE.value)
          respond result
          return
      }
    }
    def getUserAccountBalance(){
      User user = springSecurityService.currentUser
      Account userAcc = kbAccountService.getUserAccountByKey(user.getUsername());
      if(userAcc?.getAccountBalance()){
            BigDecimal accountBal = userAcc.getAccountBalance().negate();
            def account = [:];
            account.balance = accountBal.doubleValue();
            account.accountId= userAcc.getAccountId();
            session['accountId']= account.accountId;
            account.name =userAcc.getUsername();
            respond account;
            return
      }else{
          def result = [:]
          result.message ="unable to get user Account for payment please try again later "
          response.setStatus(NOT_ACCEPTABLE.value)
          respond result
          return
      }
    }
    def getUserOverdueStatus(){
      User user = springSecurityService.currentUser
    }
}
