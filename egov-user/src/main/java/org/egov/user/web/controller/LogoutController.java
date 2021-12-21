package org.egov.user.web.controller;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.domain.model.TokenWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class LogoutController {

    private TokenStore tokenStore;

    public LogoutController(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * End-point to logout the session.
     *
     * @param accessToken
     * @return
     * @throws Exception
     */
    @PostMapping("/_logout")
    public ResponseInfo deleteToken(@RequestBody TokenWrapper tokenWrapper) throws Exception {
    	System.out.println("Inside deleteToken with request data.");
    	
    	long start = System.currentTimeMillis();
    	System.out.println("Before tokenWrapper.getAccessToken() call.");
        String accessToken = tokenWrapper.getAccessToken();
        System.out.println("After tokenWrapper.getAccessToken() call."+timeDiff(start));
        
        start = System.currentTimeMillis();
        System.out.println("Before tokenStore.readAccessToken(accessToken) call.");
        OAuth2AccessToken redisToken = tokenStore.readAccessToken(accessToken);
        System.out.println("After tokenStore.readAccessToken(accessToken) call."+timeDiff(start));
        
        start = System.currentTimeMillis();
        System.out.println("Before  tokenStore.removeAccessToken(redisToken) call.");
        tokenStore.removeAccessToken(redisToken);
        System.out.println("Before  tokenStore.removeAccessToken(redisToken) call."+timeDiff(start));
        
        return new ResponseInfo("", "", new Date().toString(), "", "", "Logout successfully");
    }
    
    private long timeDiff(long start) {
    	return System.currentTimeMillis()-start;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleError(Exception ex) {
        ex.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "", "Logout failed");
        response.setResponseInfo(responseInfo);
        Error error = new Error();
        error.setCode(400);
        error.setDescription("Logout failed");
        response.setError(error);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
