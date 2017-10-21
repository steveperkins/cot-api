package org.collegeopentextbooks.api.db;

import org.collegeopentextbooks.api.Application;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

public class StringTest {


	@Test
	public void test() {
		String str = " by Illowsky,Dean   ";
		int index = str.indexOf(" by ");
		String nustring = str.replaceAll("\\wby ", "");
		System.out.println(nustring);
	}

}
