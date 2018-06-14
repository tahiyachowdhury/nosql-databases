package com.citi.ratesignite.mkv;

import com.iontrading.mkv.Mkv;
import com.iontrading.mkv.MkvChain;
import com.iontrading.mkv.MkvObject;
import com.iontrading.mkv.MkvRecord;
import com.iontrading.mkv.MkvSupply;
import com.iontrading.mkv.enums.MkvChainAction;
import com.iontrading.mkv.enums.MkvObjectType;
import com.iontrading.mkv.events.MkvChainListener;
import com.iontrading.mkv.events.MkvPublishListener;
import com.iontrading.mkv.events.MkvRecordListener;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

public class MKVListener {

    private Mkv mkv;
    private IgniteCacher igniteCacher;

    @PostConstruct
    public void start() {
        try {
           // mkv = Mkv.start().getInstance();
            mkv = Mkv.getInstance();
            mkv.getPublishManager().addPublishListener(new PublishListener());
        }
        catch (Throwable e){
            logger.error("Failed to start mkv: ", e);
        }
    }

    public void subscribeToChain(String chainName) {
        try{
            ChainListener chainListener = new ChainListener(chainName, mkv);
            mkv.getSubscribeManager().persistentSubscribe(chainName, chainListener, chainListener);
        }
        catch(Throwable e){
            logger.error("Failed to subscribe to chain: ", e);
        }

    }

    public static class ChainListener implements MkvChainListener, MkvRecordListener {

        private final String chainName;
        private final Mkv mkv;

        public ChainListener(String chainName, Mkv mkv) {
            this.chainName = chainName;
            this.mkv = mkv;
        }

        @Override
        public void onFullUpdate(MkvRecord mkvRecord, MkvSupply mkvSupply, boolean bool) {
            try {
                igniteCacher.update(chainName, mkvRecord);
            } catch (Exception e) {
                logger.error("Failed to handle full chain update for: " + mkvRecord, e);
            }
        }

        @Override
        public void onPartialUpdate(MkvRecord mkvRecord, MkvSupply mkvSupply, boolean bool) {
            logger.error("Partial update not yet configured");
        }


        @Override
        public void onSupply(MkvChain arg0, String arg1, int arg2, MkvChainAction arg3) {
            logger.error("Supply not yet configured");
        }

    }

    public class PublishListener implements MkvPublishListener {

        @Override
        public void onPublish(MkvObject mkvObject, boolean b, boolean b1) {
            if (MkvObjectType.CHAIN == mkvObject.getMkvObjectType()) {
                MKVListener.this.subscribeToChain(mkvObject.getName());
            }
            //handles records outside of a chain
            else{
                logger.error("Cannot yet handle records outside of a chain");
            }

        }

        @Override
        public void onPublishIdle(String s, boolean b) {
            logger.error("Publish idle not yet configured");
        }


        @Override
        public void onSubscribe(MkvObject mkvObject) {
            logger.error("Subscribe not yet configured");
        }
    }

    public void setIgniteCacher(IgniteCacher igniteCacher) {
        this.igniteCacher = igniteCacher;
    }

    public static Logger logger = Logger.getLogger(MKVListener.class);
}
