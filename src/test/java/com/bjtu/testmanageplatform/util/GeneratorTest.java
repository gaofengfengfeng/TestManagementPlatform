package com.bjtu.testmanageplatform.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author: gaofeng
 * @Date: 2019-07-11
 * @Description:
 */
public class GeneratorTest {

    @Test
    public void generateLongId() {
        System.out.println(Generator.generateLongId());

    }

    @Test
    public void createSalt() {
        System.out.println(Generator.createSalt());
    }


}