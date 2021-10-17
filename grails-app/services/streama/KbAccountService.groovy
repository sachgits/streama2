package streama

import grails.transaction.Transactional
import groovy.transform.InheritConstructors
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.client.model.gen.Account;
import org.killbill.billing.client.model.gen.PaymentMethod;
import org.killbill.billing.util.api.AuditLevel;
import groovy.util.logging.Slf4j;
import java.util.regex.Pattern;

@Slf4j
@Transactional
@InheritConstructors
class KbAccountService extends KbClientService {

    def serviceMethod() {

    }
    def createNewAccount(User user){
      Account body = new Account();
      body.setExternalKey(formatUsername(user.getUsername()));
      body.setTimeZone("Africa/Nairobi");
      body.setCurrency(Currency.KES);
      body.setCity("Nairobi");
      body.setCountry("Kenya");
      body.setPhone(user.getUsername());
      try{
      log.info("creating account .......");
        return accountApi.createAccount(body, requestOptions);
      }catch(KillBillClientException kbEx){
        log.error(kbEx.getMessage());
        return null;
      }

    }

    def formatUsername(String phonenumber){
      if(phonenumber[0] == "0"){
          Pattern p = Pattern.compile("0");
          return phonenumber.replaceFirst(p, "254");
      }
      if(phonenumber[0] == '+')
          return phonenumber.substring(1);
      return phonenumber;
    }

    def addAccountPaymentMethod(Account acc){
      PaymentMethod body = new PaymentMethod();
      body.setIsDefault(true);
      body.setPluginName("killbill-payment-test"); //TODO: remove this later pass it as a string
      try{
      log.info("creating account payment method .......");
        return accountApi.createPaymentMethod(acc.getAccountId(),
                                                                  body,
                                                                  true,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  requestOptions);
      }catch(KillBillClientException kbEx){
        log.error(kbEx.getMessage());
        return null;
      }
    }

    def createAccountWithDefaultPaymentMethod(User user, String pluginName){
        def userAcc = createNewAccount(user);
        if(userAcc instanceof Account){
            def paymentMeth = addAccountPaymentMethod(userAcc);
            if(paymentMeth instanceof PaymentMethod)
                return userAcc
        }
        return null;
    }

    def getUserAccountByKey(String externalKey){
          Boolean accountWithBalance = true; // Will include account balance
          Boolean accountWithBalanceAndCBA = true; // Will not include account balance and CBA info

        try{
            log.info("retrieving user account .......");
            Account acc = accountApi.getAccountByKey(formatUsername(externalKey),
                                            accountWithBalance,
                                            accountWithBalanceAndCBA,
                                            AuditLevel.NONE,
                                            requestOptions);
            //log.info(acc.getAccountBalance().intValueExact());
            return acc;
        }catch(KillBillClientException kbEx){
            log.error(kbEx.getMessage());
            return null;
        }
    }

}
