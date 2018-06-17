package com.citi.ratesignite.mkv;
import com.iontrading.mkv.Mkv;

import java.util.ArrayList;
import java.util.List;

import static javafx.beans.binding.Bindings.when;
import org.junit.test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class MKVListenerTest {

    private Mkv mkv;
    private MKVListener mkvListener;
    private IgniteCacher igniteCacher;

    @Test
    public void testMkvListener(){
        mkvListener = new MKVListener();
        beforeTest();
        startTest();
        subscribeToChainTest();
    }



    public void startTest() {
        assertEquals(true, mkvListener.start());
    }


    public void subscribeToChainTest() {
        assertEquals(true, mkvListener.subscribeToChain("USD.ANALYTICS_PRICE.ANALYTICS.PRICES"));
    }


    public void onFullUpdateTest() {
        assertEquals(true, mkvListener.onFullUpdate("USD.ANALYTICS_PRICE.ANALYTICS.PRICES", new MkvRecord()));
    }


    
    public void beforeTest() throws Exception{
        mkv = mock(Mkv.class);
        igniteCacher = mock(IgniteCacher.class);
        when(igniteCacher.update()).thenReturn(constructMkv());

        List<String> chain = constructChain();

        String chainName = "USD.ANALYTICS_PRICE.ANALYTICS.PRICES";

        when(mkv.start.getInstance()).thenReturn(true);

        MKVListener.ChainListener chainListener = new MKVListener.ChainListener(chainName, mkv);
        when(mkv.getSubscribeManager().persistentSubscribe
                (chainName, chainListener, chainListener)).
                thenReturn(chain);
        
        when(igniteCacher.update(chainName, any(MkvRecord.class))).thenReturn(true);
    }

    /*
    public List<String> constructChain(){
        List<String> mockChain = new ArrayList<>();
        mockChain.add("Rec1");
        mockChain.add("Rec2");
        mockChain.add("Rec3");

        return mockChain;
    }*/

}
