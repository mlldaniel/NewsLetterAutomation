/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author danielhwang
 */
public class NewClass {
    public static void main(String[] args) {
        String dbStr = "<dbID>2221</dbID>";
        dbStr = dbStr.replaceAll("<dbID>.+</dbID>", "<dbID>5</dbID>");
        System.out.println(dbStr);
    }
}
