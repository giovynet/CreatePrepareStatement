package com.giovyenet.utils.jdbc;

import java.util.List;

public class CreatePS {	
	
	public static void main(String[] args) {
		String sentence = "" + 
				"	id                   bigint NOT NULL ,\r\n" + 
				"	periodo_cierre       date  NOT NULL ,\r\n" + 
				"	fecha_ejecucion_cierre date   ,\r\n" + 
				"	en_ejecucion         bool   ,\r\n" + 
				"	fecha_ejecucion_deterioro date   ,\r\n" + 
				"	fecha_ejecucion_calculo_fianza date"; 		
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
		

		System.out.println("=== Getter & Setters PrepareStatement ===");
		List <String> lstPSNoPrefix = codeStatement.getLstPS(null);
		for (String element : lstPSNoPrefix) {
			System.out.println(element);			
		}
		
		
		
		List <String> lstPS = codeStatement.getLstPS("dto");
		for (String element : lstPS) {
			System.out.println(element);			
		}

		List <String> lstRSNoPrefix = codeStatement.getLstRS("dto");
		for (String element : lstRSNoPrefix) {
			System.out.println(element);			
		}
		
		
	}
	
	
	
}
