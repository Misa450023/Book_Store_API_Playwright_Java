package rest;

import java.util.HashMap;

public class Context {
	
// *** HTTP Request Context
	
public String url;
public String contentType;


public HashMap<String,String>pathParams=new HashMap<String,String>();
public HashMap<String,String>queryParams=new HashMap<String,String>();
public HashMap<String,String>headerParams=new HashMap<String,String>();
public String body;

}
