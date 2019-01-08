package com.nathan.service;

import com.nathan.compressor.Compressor;

import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class EventProcessor {
    @ServiceActivator(inputChannel = "kinesisReceiveChannel", poller = {@Poller(fixedRate = "1000")} )
    /*public void onEvent(String event) {
        System.out.println("Event Recieved by thread id : " + Thread.currentThread().getId());
        System.out.println("event: " + event);

    }*/

    public void onEvent(ByteArrayInputStream event) {
        System.out.println("Event Recieved by thread id : " + Thread.currentThread().getId());
        OutputStream gZIPOutputStream = null;
        InputStream gZIPInputStream = null;
        System.out.println("The data before decompressing : " + event);
        System.out.println("The data after decompressing : " + new String(Compressor.decompress(event)));

        /*try {
            gZIPInputStream =  new GZIPInputStream(event);


            byte[] temp = new byte[1024];
            int len;
            gZIPOutputStream = new GZIPOutputStream(new FileOutputStream("temp.gz"));
            //gZIPOutputStream.write(Compressor.decompress(event));
            while((len = gZIPInputStream.read(temp))>=0){
                gZIPOutputStream.write(temp, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if (gZIPInputStream != null) {
                try {
                    gZIPInputStream.close();
                }catch (IOException e){

                }
            }
            if (gZIPOutputStream != null) {
                try {
                    gZIPOutputStream.close();
                }catch (IOException e){

                }
            }
        }
        try {
            //XMLToJSON.XML2JSON(new ByteArrayInputStream(Compressor.decompress(event)));
            //System.out.println("The data after decompressing : " + new String(Compressor.decompress(event)));
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
