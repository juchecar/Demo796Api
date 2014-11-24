package com.snapfish.publisher;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Properties;

/**
 * Snapfish Publisher OAuth1.0a client program.
 * This program is designed to demonstrate how to work with Snapfish Publisher OAuth1.0a APIs
 * 
 * @author Ganesh Guttikonda
 *
 */
public class OAuthClient {
    
    
    /**
     * Test the stuff!!
     * @param args
     */
    public static void main(String[] args) throws Exception {
        
        /**
         * If your company goes through proxy!!
         
        Properties properties = System.getProperties();
        properties.put("http.proxyHost", "<your-company-http-proxy>");
        properties.put("http.proxyPort", "<your-company-http-proxy-port>");
        properties.put("https.proxyHost", "<your-company-https-proxy>");
        properties.put("https.proxyPort", "<your-company-https-proxy-port>");
        */
     
        
        /**
         * The key/secret that are being distributed as part of this app are only for test purposes.
         * Your app should have it's own app key & secret!
         */
        OAuthClient client =
                    new OAuthClient("8a62c7a97e834d3ea730a1cf33e6aa2f", "9f5961c677b540f290bfdaba739a6165", 
                            "https://openapi.sfint1.qa.snapfish.com/services/request_token", 
                            "https://openapi.sfint1.qa.snapfish.com/services/access_token");
        Map<String,String> requestTokenData = client.getRequestToken();
        
        System.out.println("Request Token Data="+ requestTokenData);
    }

    
    
    /**
     * Get a request token for Snapfish API.
     * @return Map of key/value pairs.
     * @throws Exception
     */
    public Map<String,String> getRequestToken() 
        throws Exception
    {
        
        URL url = new URL(requestTokenEndPoint);
        URLConnection uc = url.openConnection();
        uc.setConnectTimeout(0);
        uc.setReadTimeout(0);
        uc.setDoOutput(true);
        OutputStreamWriter uco = new OutputStreamWriter
            (uc.getOutputStream());

        Request op = new Request("POST");
        op.addParameter("oauth_consumer_key", key);
        String cburl = "http://localhost:7070/mycallbackurl"; // your app callback URL, because this is where Publisher redirects after logging in.
        
        op.addParameter("oauth_callback", cburl);
        
        /**
         * The 3rd parameter of the following api call is null, because request token API call will never have token-secret.
         */
        op.sign(requestTokenEndPoint, secret, null);
        uco.write(op.toString());// flush the normalized string to url connection.
        uco.close();

        //make sure you save these variable for the current user session. It has following things.
        // sf_request_auth_url -> To where you should be redirecting the browser so that user can login to snapfish
        // oauth_token -> Request Token [ Important! Needed while making a access-token request, after the login ]
        // oauth_token_secret -> Request Token secret [ Important! Needed while making a access-token request, after the login ]
        return Utils.readParams(uc.getInputStream());
        
    }

    
    
    /**
     * Use the following method to get an access token from Publisher server!!
     * Note: You must pass the requestToken, requestTokenSecret, and requestTokenVerifier that Publisher gives to you.
     *       Complete the following steps before calling this method.
     *          - Request a request token 
     *          - Get the 'sf_request_auth_url' from response of request token
     *          - Redirect the browser to 'sf_request_auth_url' from Publisher
     *          - Snapfish serves the login page, and user logs-in.
     *          - After the customer logs in successfully, Publisher redirects back to the app at the 'oauth_callback' URL you provided while making the request token call.
     *          - When the redirect happens, the callback URL will contain the following parameters
     *              - oauth_token
     *              - oauth_token_verifier
     *          - The callback is the authorization hand-off from Publisher to your app.
     *          - Your app must retrieve oauth_token and oauth_token_verifier parameters from the URL.
     *              IMPORTANT: When you retrieve the oauth_token and oauth_token_verifier parameters from the URL, make sure the parameters are decoded correctly because most java servlet containers 
     *                     supply default decoding [ when calling HttpServletRequest.getParmeter('oauth_token') ]. This may differ from server-to-server implementation.
     *                     In other words, make sure you send exactly what Publisher has passed to you. com.snapfish.publisher.Request.sign(..) takes care of encoding the parameters for you.
     *                     
     *          - Once you retrieve the above OAuth parameters, you should be able to make an API call to Publisher to retrieve an access-token.
     *          
     *          
     * @param requestToken
     * @param requestTokenSecret
     * @param requestTokenVerifier
     * @return Map<String,String> params from Snapfish containing access-token and access-token-secret.
     * @throws Exception 
     */
    public Map<String,String> getAccessToken(
            String requestToken, 
            String requestTokenSecret,
            String requestTokenVerifier) 
            throws Exception
    {
        URL url = new URL(accessTokenEndPoint);
        URLConnection uc = url.openConnection();
        uc.setConnectTimeout(0);
        uc.setReadTimeout(0);
        uc.setDoOutput(true);
        OutputStreamWriter uco = new OutputStreamWriter
            (uc.getOutputStream());

        Request op = new Request("POST");
        op.addParameter("oauth_consumer_key", key);
        op.addParameter("oauth_token", requestToken);
        op.addParameter("oauth_verifier", requestTokenVerifier);
        op.sign(key, secret, requestTokenSecret);
        uco.write(op.toString());
        uco.close();

        Map<String,String> params = Utils.readParams(uc.getInputStream());
        //Save the following parameters to the current browsing user session! Your app will need them for making API calls.

        String accessToken = Utils.verifyGet("oauth_token", params);
        String accessTokenSecret = Utils.verifyGet("oauth_token_secret", params);
        String restEndPoint = Utils.verifyGet("sf_opensocial_rest_url", params);
        String uploadEndPoint = Utils.verifyGet("sf_upload_url", params);
        return params;
        
    }
    

    /// Some utility methods
    /**
     * Return the consumer key
     * @return String
     */
    public String getConsumerKey() {
        return key ;
    }
    /**
     * Return the consumer secret
     * @return String
     */
    public String getConsumerSecret() {
        return secret;
    }
    /**
     * Return the request token end point
     * @return String
     */
    public String getRequestTokenEndPoint() {
        return requestTokenEndPoint ;
    }
    /**
     * Return access token end point
     * @return String
     */
    public String getAccessTokenEndPoint() {
        return accessTokenEndPoint;
    }
    /**
     * The Consumer key
     */
    private final String key ;
    /**
     * The Consumer secret
     */
    private final String secret ;
    /**
     * OAuth1.0a request token end point, provided by Snapfish
     */
    private final String requestTokenEndPoint ;
    /**
     * OAuth1.0a access token end point, provided by Snapfish
     */
    private final String accessTokenEndPoint;
    
    
    /**
     * The default constructor.
     * @param k : Consumer key
     * @param s : Consumer secret
     * @param reqTokenEndPt : RequestToken end point
     * @param accTokenEndPt : AccessToken end point
     */
    public OAuthClient(String k, String s, String reqTokenEndPt,String accTokenEndPt) {
        key = k ;
        secret = s;
        requestTokenEndPoint = reqTokenEndPt ;
        accessTokenEndPoint = accTokenEndPt ;
    }
    
    
   
}// end of the class
