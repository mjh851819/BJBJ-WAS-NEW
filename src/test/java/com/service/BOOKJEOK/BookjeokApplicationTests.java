package com.service.BOOKJEOK;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

//@SpringBootTest
class BookjeokApplicationTests {

	@Test
	void contextLoads() {
	}

	public enum e {

		a("가"), b("나"), c("다");

		private String value;

		e(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@Test
	public void list_test() throws Exception {

	    //given
	    String str = "a,b,c";
	    //when
		List<String> tagList = Arrays.asList(str.split(","));
	    //then

		for (int i = 0; i < tagList.size(); i++) {
			System.out.println(tagList.get(i));
		}
	}



}
