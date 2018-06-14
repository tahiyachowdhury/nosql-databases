package com.citi.ratesignite.mkv;

import com.iontrading.mkv.MkvRecord;
import com.iontrading.mkv.MkvType;
import com.iontrading.mkv.MkvValue;
import com.iontrading.mkv.enums.MkvFieldType;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.binary.BinaryObjectBuilder;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ConnectorConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IgniteCacher implements Serializable {

    private Ignite ignite = null;
    private Map<String, IgniteCache<String, BinaryObject>> cacheMap = new HashMap<>();

    public void update(String chainNameDot, final MkvRecord mkvRecord) {
        try {

            if (ignite == null) {
                ignite = Ignition.start("default-config.xml");
                ConnectorConfiguration connectorConfiguration = new ConnectorConfiguration();
                connectorConfiguration.setJettyPath("jetty.xml");
            }
            final String chainName = chainNameDot.replace('.', '_');
            final MkvType mkvType = mkvRecord.getMkvType();
            IgniteCache<String, BinaryObject> cache;
            if (!cacheMap.containsKey(chainName)) {

                final LinkedHashMap<String, String> fields = new LinkedHashMap<>();
                fields.put("RecordId", "java.lang.String");
                for (int i = 0; i < mkvType.size(); ++i) {
                    String fieldName = mkvType.getFieldName(i);
                    MkvValue mkvValue = mkvRecord.getValue(fieldName);
                    if (mkvValue.getType() == MkvFieldType.INT) {
                        fields.put(fieldName, "java.lang.Integer");
                    } else if (mkvValue.getType() == MkvFieldType.REAL) {
                        fields.put(fieldName, "java.lang.Double");
                    } else if (mkvValue.getType() == MkvFieldType.STR) {
                        fields.put(fieldName, "java.lang.String");
                    } else if (mkvValue.getType() == MkvFieldType.DATE){
                        fields.put(fieldName, "java.util.Date");
                    }
                    else if(mkvValue.getType() == MkvFieldType.TIME){
                        fields.put(fieldName, "java.util.Time");
                    }
                    else{
                        logger.error("Record value type does not match one of the existing Mkv field types");
                    }
                }

                CacheConfiguration<String, BinaryObject> config = new CacheConfiguration(chainName);
                config.setQueryEntities(new ArrayList<QueryEntity>() {{
                    QueryEntity queryEntity = new QueryEntity();
                    queryEntity.setKeyType("java.lang.String");
                    queryEntity.setValueType(chainName);
                    queryEntity.setFields(fields);
                    add(queryEntity);
                }});
                cache = ignite.getOrCreateCache(config).withKeepBinary();
                cacheMap.put(chainName, cache);
            } else {
                cache = cacheMap.get(chainName);
            }
            BinaryObjectBuilder objectBuilder = ignite.binary().builder(chainName);
            objectBuilder.setField("RecordId", mkvRecord.getName());
            for (int i = 0; i < mkvType.size(); ++i) {
                String fieldName = mkvType.getFieldName(i);
                MkvValue mkvValue = mkvRecord.getValue(fieldName);
                if (mkvValue.getType() == MkvFieldType.INT) {
                    objectBuilder.setField(fieldName, mkvValue.getInt());
                } else if (mkvValue.getType() == MkvFieldType.REAL) {
                    objectBuilder.setField(fieldName, mkvValue.getReal());
                } else if (mkvValue.getType() == MkvFieldType.STR) {
                    objectBuilder.setField(fieldName, mkvValue.getString());
                }
                //log the fact there is no type match
                else{
                    logger.error("Record value type does not match one of the existing Mkv field types");
                }
            }
            cache.put(mkvRecord.getName(), objectBuilder.build());

        } catch (Throwable e) {

            logger.error("Failed to update ignite cache: ", e);
        }
    }
    public static Logger logger = Logger.getLogger(IgniteCacher.class);
}

//http://localhost:8080/ignite?cmd=qryfldexe&pageSize=1000&cacheName=USD_THETA_SOD_RISK_THETA&qry=select+*+from+USD_THETA_SOD_RISK_THETA


//    select inst.Cusip, inst.PricingType, price.MidPrice, price.Yield
//        from
//        USD_PRICING_INSTRUMENT_ANALYTICS_INSTRUMENTS inst,
//        "USD_ANALYTICS_PRICE_ANALYTICS_PRICES".USD_ANALYTICS_PRICE_ANALYTICS_PRICES price
//        where
//        inst.Cusip = price.Cusip and inst.PricingType = 'BMC'