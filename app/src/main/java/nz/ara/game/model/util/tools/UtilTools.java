/**
 * 
 */
package nz.ara.game.model.util.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author yac0105
 *
 */
public class UtilTools {

	public static boolean isBlank(String str) {
		if(str== null || str.trim().length()==0) {
			return true;
		}
		
		return false;
	}
	
	public static boolean equal(String str1, String str2) {

		if(str1==null && str2==null) {
			return true;
		}else if(str1==null || str2==null) {
			return false;
		}else {
			return str1.trim().equals(str2.trim());
		}
		
	}
	
	public static String stringToAscii(String value)  
	{  
	    StringBuffer sbu = new StringBuffer();  
	    char[] chars = value.toCharArray();   
	    for (int i = 0; i < chars.length; i++) {  
	        if(i != chars.length - 1)  
	        {  
	            sbu.append((int)chars[i]).append(",");  
	        }  
	        else {  
	            sbu.append((int)chars[i]);  
	        }  
	    }  
	    return sbu.toString();  
	}  
	
	public static Object copyObj(Object obj) throws Exception{
	    //class object
	    Class<?> classType=obj.getClass();
	    //a copy
	    Object stu1=classType.newInstance();
	    for(Field field:classType.getDeclaredFields()){

	         String getMethodName="get"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
	         String setMethodName="set"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);

	         Method getMethod=classType.getDeclaredMethod(getMethodName, new Class[]{});
	         Object value=getMethod.invoke(obj, new Object[]{});
	            
	         Method setMethod=classType.getDeclaredMethod(setMethodName, new Class[]{field.getType()});
	         setMethod.invoke(stu1, new Object[]{value});
	    }

	    return stu1;
	}
	
}
