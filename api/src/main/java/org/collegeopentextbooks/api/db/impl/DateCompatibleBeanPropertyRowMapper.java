package org.collegeopentextbooks.api.db.impl;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.BeanWrapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class DateCompatibleBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
	
	@Override
    protected void initBeanWrapper(BeanWrapper bw) {
		super.initBeanWrapper(bw);
	    bw.setConversionService(new ConversionService() {
		        @Override
		        public boolean canConvert(Class<?> aClass, Class<?> aClass2) {
		            return aClass == java.sql.Date.class && aClass2 == LocalDate.class;
		        }
		
		        @Override
		        public boolean canConvert(TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2) {
		            return canConvert(typeDescriptor.getType(), typeDescriptor2.getType());
		        }
		
		        @Override
		        public <T> T convert(Object o, Class<T> tClass) {
		            if(o instanceof Date && tClass == LocalDate.class)
		            {
		                return (T)LocalDate.from(((Date)o).toInstant());
		            }
		
		            return null;
		        }
	
		    @Override
		    public Object convert(Object o, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2) {
		        return convert(o,typeDescriptor2.getType());
		    }
	    });
	}
}
