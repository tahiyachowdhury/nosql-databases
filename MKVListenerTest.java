package com.citi.ratesignite.mkv;
import com.iontrading.mkv.Mkv;

import java.util.ArrayList;
import java.util.List;

import static javafx.beans.binding.Bindings.when;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class MKVListenerTest {

    private Mkv mkv;
    private MKVListener mkvListener;
    private IgniteCacher igniteCacher;

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
        assertEquals(true, mkvListener.start());
    }


    public void onFullUpdateTest() {
        assertEquals();
    }



    public void beforeTest(){
        mkv = mock(Mkv.class);
        igniteCacher = mock(IgniteCacher.class);
        when(igniteCacher.update()).thenReturn(constructMkv());

        List<String> chain = constructChain();

        String chainName = "USD.ANALYTICS_PRICE.ANALYTICS.PRICES";

        when(mkv.getInstance()).thenReturn(constructMkv());

        MKVListener.ChainListener chainListener = new MKVListener.ChainListener(chainName, mkv);
        when(mkv.getSubscribeManager().persistentSubscribe
                (chainName, chainListener, chainListener)).
                thenReturn(chain);
    }

    public boolean constructMkv(){
        return true;
    }
    public List<String> constructChain(){
        List<String> mockChain = new ArrayList<>();
        mockChain.add("Rec1");
        mockChain.add("Rec2");
        mockChain.add("Rec3");

        return mockChain;
    }




}