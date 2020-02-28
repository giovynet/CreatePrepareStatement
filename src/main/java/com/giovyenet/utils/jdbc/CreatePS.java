package com.giovyenet.utils.jdbc;

import java.util.List;

public class CreatePS {	
	
	
	static String prefixDto = "dto";
	
	public static void main(String[] args) {
	
		
		String sentence = "" + 
				"id                   bigint  NOT NULL ,\r\n" + 
				"numero_cuenta        varchar NOT NULL ,\r\n" + 
				"tipo_cuenta          varchar NOT NULL ,\r\n" + 
				"banco                varchar NOT NULL ,\r\n" + 
				"nombre_titular       varchar NOT NULL ,\r\n" + 
				"apellido_titular     varchar NOT NULL ,\r\n" + 
				"departamento         varchar NOT NULL  ,\r\n" + 
				"ciudad               varchar NOT NULL  ,\r\n" + 
				"id_preinscripcion_cuenta integer  NOT NULL ,\r\n" + 
				"fecha                timestamp  NOT NULL ,\r\n" + 
				""; 		
	
		System.out.println(sentence);
		
		CodeStatement codeStatement = new CodeStatement(sentence);
		
		List<Field> lstField = codeStatement.getLstField();
		
		for (Field f : lstField) {
			System.out.println("--");
			System.out.print(f.getJavaName() + ",");
			System.out.print(f.getSqlName() + ",");
			System.out.print(f.getJavaType() + ",");
			System.out.print(f.getSqlType() + ",");
			System.out.print(f.getNotNull() + ",");
		}
		System.out.println("\r\n=== Atributes to DTO ===");
		
		List <String> lstDTOFields = codeStatement.getLstDTOFields();
		for (String element : lstDTOFields) {
			System.out.println(element);			
		}
		
		System.out.println("=== Set PS from  ===");
		List <String> lstPSNoPrefix = codeStatement.getLstPS(null);
		for (String element : lstPSNoPrefix) {
			System.out.println(element);			
		}
		
		System.out.println("=== Set PS from DTO ===");
		List <String> lstPS = codeStatement.getLstPS(prefixDto);
		for (String element : lstPS) {
			System.out.println(element);			
		}
		
		System.out.println("=== Set DTO from PS ===");
		List <String> lstRSNoPrefix = codeStatement.getLstRS(prefixDto);
		for (String element : lstRSNoPrefix) {
			System.out.println(element);			
		}
		
		
	}
	
	
	
}
