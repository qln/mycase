package com.qln.cases.shiro.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

public class CustomCredentialsMatcher extends HashedCredentialsMatcher {

    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        return super.doCredentialsMatch(authcToken, info);
    }
}
