/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Gitu
 */
public class RSA {

    /**
     * @param args the command line arguments
     */
    private static Object[] decrKeys;
    
    //to check if number is a prime number
    private static int checkPrime(int n) {
        int i,m=0,flag=0;      
        m=n/2;      
        if(n==0||n==1){  
            System.out.print(" ");      
        } else {  
            for(i=2;i<=m;i++){      
                if(n%i==0){      
                flag=1;      
                break;      
                }   
            }
        }
        return flag;
    }
    
    //to claculate the GCD
    private static int calculateGcd(int a, int b) { 
        if (a == 0) 
            return b; 
        return calculateGcd(b % a, a); 
    } 
    
    //to calculate the Totient
    private static int calculateTotient(int n) 
    { 
        int result = 1; 
        for (int i = 2; i < n; i++) 
            if (calculateGcd(i, n) == 1) 
                result++; 
        return result; 
    } 
    
    //to select an encryption key
    private static int selectEncryptionKey(int totient) {
        int e = 0;
        for (int i = 2; i < 10; i++)
            if (calculateGcd(i, totient) == 1) {
                e = i;
                break;
            }
        return e;
    }
    
    //to create list of possible decryption keys from encryption key
    private static void selectDecryptionKeys(float e, float totient) {
        ArrayList numbers = new ArrayList();
        int count = 0;
	float k = 0;
	do {
            float d = (1+k*totient)/e;
            if((d - Math.floor(d)) == 0) {
                    count++;
                    numbers.add((int)d);
		}
		k++;
 	}while (count < 3);
        
        decrKeys = numbers.toArray();
    }
    
    //to encrypt or decrypt the message
    private static int convert(int value, int key, int n) {
        BigInteger result = BigInteger.valueOf(value).modPow(BigInteger.valueOf(key), BigInteger.valueOf(n));
        return result.intValue();
    }
    
    private static void test(int e, int n) {
        for (int i=0; i<decrKeys.length; i++) {
            System.out.println("\nEncrypt/Decryption pair " + (i+1) + ": ");
            System.out.println("PU {e,n}: " + e + "," + n);
            System.out.println("PR {d,n}: " + decrKeys[i] + "," + n);

            int cypher = convert(6, e, n);
            System.out.println("Encryption: M = 6; C = " + cypher);
            System.out.println("Decryption: C = " + cypher + "; M = " + convert(cypher, (int) decrKeys[i], n));
        }
    }

    public static void main(String[] args) {
        ArrayList numbers = new ArrayList();
        Random rand = new Random(); 
        int p = 0, q = 0, n = 0, totient = 0;
        //to generate prime numbers between 1 and 50
        for (int i=1; i<=50; i++) {
            int x = checkPrime(i); 
            if ( x == 0) {
                numbers.add(i);
            }
        }
        
        Object[] num = numbers.toArray();
        p = Integer.parseInt(num[rand.nextInt(numbers.size())].toString());
        //to select random prime number != p
        boolean condition = true;
        do {
            int randomPrime = Integer.parseInt(num[rand.nextInt(numbers.size())].toString());
        
            if (p != randomPrime) { 
                q = randomPrime;
                condition = false;
            }
        } while (condition);

        n = p * q;
        totient = calculateTotient(n);
        int e = selectEncryptionKey(totient);
        selectDecryptionKeys(e, totient);
        
        System.out.print("Prime Numbers: ");
        for (Object x : num) {
            System.out.print(x + " "); 
        }
        System.out.println("\nn = " + n);
        System.out.println("totient = " + totient);
        System.out.println("p = " + p);
        System.out.println("q = " + q);
        
        test(e, n);
    }
    
}
