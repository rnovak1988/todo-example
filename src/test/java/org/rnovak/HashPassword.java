package org.rnovak;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Scanner;

/**
 * Created by rn020584 on 9/29/15.
 */
public class HashPassword {

    public static void main (String[] args) {

        System.out.print("password: ");
        String password = (new Scanner(System.in)).nextLine();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode(password));

    }

}
