package com.leisucn.test.nettydemo.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by sulei on 2017/8/29.
 */
public class PlainOioServer {

    private final Logger logger = LogManager.getFormatterLogger((getClass()));

    public void serve(int port) throws IOException{

        final ServerSocket socket = new ServerSocket(port);

        try{
            for(;;){
                final Socket clientSocket = socket.accept();
                logger.info("Accepted connection from %s", clientSocket);

                new Thread( () -> {
                    OutputStream out;

                    try{
                        //Thread.currentThread().sleep(500);
                        out = clientSocket.getOutputStream();
                        out.write("Hi\r\n".getBytes(Charset.forName("UTF-8")));
                        out.flush();
                    } catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }).start();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception{
        new PlainOioServer().serve(9099);
    }
}
