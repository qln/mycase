package com.qln.cases.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class ShiroSessionListener implements SessionListener {

    @Override
    public void onStart(Session session) {
        // TODO Auto-generated method stub
        System.out.println("on start...");
    }

    @Override
    public void onStop(Session session) {
        // TODO Auto-generated method stub
        System.out.println("on stop...");
    }

    @Override
    public void onExpiration(Session session) {
        // TODO Auto-generated method stub
        System.out.println("on expiration...");
    }

}
