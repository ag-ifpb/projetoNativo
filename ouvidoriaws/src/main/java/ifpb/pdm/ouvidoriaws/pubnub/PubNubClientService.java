/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.pubnub;

import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import ifpb.pdm.ouvidoriaws.entities.Ticket;
import javax.ejb.Stateless;
import org.json.JSONObject;

/**
 *
 * @author natarajan
 */

@Stateless
public class PubNubClientService {
    
    private static final String PUBNUB_PUBTOKEN = "pub-c-a6e1e709-c24f-4608-ab08-0cacf634bcfd";
    private static final String PUBNUB_SUBTOKEN = "sub-c-bd52fa58-a96e-11e7-a097-061ce3592077";

    private static PubNub pubnub = null;
    private static PubNubClientService instance = null;
    
    private static void init() {
        //
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(PUBNUB_SUBTOKEN);
        pnConfiguration.setPublishKey(PUBNUB_PUBTOKEN);
        pnConfiguration.setSecure(false);
        //
        pubnub = new PubNub(pnConfiguration);
        instance = new PubNubClientService();
    }
    
    public static PubNubClientService instance(){
        init();
        return instance;
    }
    
    
    public void sendMessageCreatedTicket(Ticket ticket) {
        
        System.out.println("Enviando notificação pubnub. Criação do ticket: " + ticket.toString());
        
        JsonObject json = new JsonObject();
        json.addProperty("type", "ticket");
        json.addProperty("idticket", ticket.getId().toString());
//        
//        JSONObject json = new JSONObject();
//        json.put("type", "ticket");
//        json.put("idticket", ticket.getId().toString());
        //  
        pubnub.publish()
                .channel("auditor")
//                .shouldStore(Boolean.TRUE)
                .message(json).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if (status.isError()) {
                    // something bad happened.
                    System.out.println("error happened while publishing: " + status.toString());
                } else {
                    System.out.println("publish worked! timetoken: " + result.getTimetoken());
                }
            }

        });
    }
    
    public void sendMessageResponseByClient(Ticket ticket) {
        
        System.out.println("Enviando notificação de ticket respondido pelo cliente: " + ticket.toString());
        
        JsonObject json = new JsonObject();
        json.addProperty("type", "response");
        json.addProperty("idticket", ticket.getId().toString());

        pubnub.publish()
                .channel("auditor")
                .message(json).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if (status.isError()) {
                    // something bad happened.
                    System.out.println("error happened while publishing: " + status.toString());
                } else {
                    System.out.println("publish worked! timetoken: " + result.getTimetoken());
                }
            }

        });
    }
    
    public void sendMessageResponseByAuditor(Ticket ticket) {
        
        System.out.println("Enviando notificação de ticket respondido pelo cliente: " + ticket.getId());
        
        JsonObject json = new JsonObject();
        json.addProperty("type", "response");
        json.addProperty("idticket", ticket.getId().toString());

        pubnub.publish()
                .channel(ticket.getFrom().getEmail())
                .message(json).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                if (status.isError()) {
                    // something bad happened.
                    System.out.println("error happened while publishing: " + status.toString());
                } else {
                    System.out.println("publish worked! timetoken: " + result.getTimetoken());
                }
            }

        });
    }

}
