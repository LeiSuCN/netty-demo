package com.leisucn.test.nettydemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sulei on 2017/8/28.
 */
public class Main {

    public static Logger logger = LogManager.getLogger(Main.class);


    public static void main(String[] args){

        logger.entry("Main", args);

    }
}
