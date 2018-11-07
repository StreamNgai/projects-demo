package dev.ngai.fantasticservice.task;

import junit.framework.TestCase;

/**
 * Des:
 * Created by Weihl
 * 2017/7/3
 */
public class XinGanTaskTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void runPageDiscover(){

        new Thread(new Mm131Task()).start();

    }

}