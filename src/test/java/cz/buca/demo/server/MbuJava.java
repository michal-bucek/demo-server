package cz.buca.demo.server;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MbuJava {

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		System.out.println(encoder.encode("12345"));

	}

}
