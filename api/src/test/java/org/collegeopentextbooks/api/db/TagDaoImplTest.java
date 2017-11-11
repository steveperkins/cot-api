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

//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = { Application.class} )
//@WebMvcTest
public class TagDaoImplTest {

	@Autowired
	TagDao dao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() {
		Tag tag = new Tag();
		tag.setName("Zippy tag");
		tag.setTagType(TagType.GENERAL);
//		dao.save(tag);
//		fail("Not yet implemented");
	}

}
