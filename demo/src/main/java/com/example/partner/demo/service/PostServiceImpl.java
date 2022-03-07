package com.example.partner.demo.service;

import java.io.InputStream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Service;

import com.example.partner.demo.utils.EncryptDecryptUtils;

@Service
public class PostServiceImpl implements PostService {

	@Override
	public String sendMessageToAWS() throws Exception {

		String isoMessage = createISOMessage();
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost("https://ou4ml2obsc.execute-api.us-east-1.amazonaws.com/dev/send-iso-message");
	    
	    String json ="{\"iso_message\":"+"\""+EncryptDecryptUtils.encryptMessage(isoMessage)+"\""+"}";
	    
	    HttpEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
	    
	    httpPost.setEntity(stringEntity);
	    
	    HttpResponse response = httpclient.execute(httpPost);
	    
	    String strResponse = EntityUtils.toString(response.getEntity());
	    
	    return formatJson(strResponse);
	}
	
	private String createISOMessage() throws Exception {
        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0200");

            isoMsg.set(3, "000010");
            isoMsg.set(4, "1500");
            isoMsg.set(7, "1206041200");
            isoMsg.set(11, "000001");
            isoMsg.set(41, "12340001");
            isoMsg.set(49, "840");
            printISOMessage(isoMsg);

            byte[] isoMsgB = isoMsg.pack();
            return new String(isoMsgB);
        } catch (ISOException e) {
            throw new Exception(e);
        }
    }
	
	private void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.printf("MTI = %s%n", isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
	
	private String formatJson(String json) {
		return StringEscapeUtils.unescapeJava(json).substring( 1, StringEscapeUtils.unescapeJava(json).length() - 1 );
	}

}
