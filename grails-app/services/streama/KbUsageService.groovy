package streama

import grails.transaction.Transactional
import groovy.transform.InheritConstructors
import org.killbill.billing.client.KillBillClientException;
import groovy.util.logging.Slf4j;
import com.google.common.collect.ImmutableList;

import org.joda.time.LocalDate;
import org.killbill.billing.client.model.gen.RolledUpUsage;
import org.killbill.billing.client.model.gen.UsageRecord;
import org.killbill.billing.client.model.gen.UnitUsageRecord;
import org.killbill.billing.client.model.gen.SubscriptionUsageRecord;
import org.killbill.billing.client.model.gen.Subscription;
import org.killbill.billing.client.model.gen.Account;

import java.util.UUID;
import java.util.HashMap;

@Slf4j
@Transactional
@InheritConstructors
class KbUsageService extends KbClientService{

    def serviceMethod() {
    }

    def recordSubscriptionUsage(UUID subscription_Id){

        UsageRecord usageRecord1 = new UsageRecord();
        usageRecord1.setAmount(1L);
        usageRecord1.setRecordDate(LocalDate.now());

        UnitUsageRecord unitUsageRecord = new UnitUsageRecord();
        unitUsageRecord.setUnitType("movies");
        unitUsageRecord.setUsageRecords(ImmutableList.<UsageRecord>of(usageRecord1));

        SubscriptionUsageRecord usage = new SubscriptionUsageRecord();
        usage.setSubscriptionId(subscription_Id);
        usage.setUnitUsageRecords(ImmutableList.<UnitUsageRecord>of(unitUsageRecord));
        try{
            usageApi.recordUsage(usage,
                                requestOptions);
        }catch(KillBillClientException clientExep){
            log.error(kbEx.getMessage());
            return null
        }
    }

}
