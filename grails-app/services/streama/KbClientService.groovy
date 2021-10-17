package streama

import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.client.KillBillClientException;
import org.killbill.billing.client.KillBillHttpClient;
import org.killbill.billing.client.RequestOptions;

import org.killbill.billing.client.api.gen.SubscriptionApi;
import org.killbill.billing.client.api.gen.AccountApi;
import org.killbill.billing.client.api.gen.CatalogApi;
import org.killbill.billing.client.api.gen.UsageApi;

import java.util.HashMap;

class KbClientService {

    KillBillHttpClient client;
    RequestOptions requestOptions;

    AccountApi accountApi;
    SubscriptionApi subscriptionApi;
    CatalogApi catalogApi;
    UsageApi usageApi;
    HashMap<String, String> NULL_PLUGIN_PROPERTIES = new HashMap<String, String>();

    KbClientService(){
        this.client = new KillBillHttpClient(String.format("http://%s:%d","3.70.28.20",8080),
                                                "admin",
                                                "password",
                                                "bob",
                                                "lazar");
        this.requestOptions = RequestOptions.builder()
                                            .withCreatedBy("wiflixAdmin") //todo: this should come from wiflix affiliates
                                            .build();
        this.accountApi = new AccountApi(client);
        this.subscriptionApi = new  SubscriptionApi(client);
        this.catalogApi = new CatalogApi(client);
        this.usageApi= new UsageApi(client);
    }
    KbClientService(String host,String username, String password,String apiKey, String apiSecret){
        this.client = new KillBillHttpClient(host,username,password,apiKey,apiSecret);
      this.requestOptions = RequestOptions.builder()
                                          .withCreatedBy("wiflix user")
                                          .build();
      this.accountApi = new AccountApi(client);
      this.subscriptionApi = new  SubscriptionApi(client);
      this.catalogApi = new CatalogApi(client);
      this.usageApi= new UsageApi(client);
        }

    def serviceMethod() {

    }
}
