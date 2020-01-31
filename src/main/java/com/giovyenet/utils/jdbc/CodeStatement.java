package com.giovyenet.utils.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CodeStatement {

	private List<Field> lstField;
	public static final String pg_type_numeric = "numeric";
	public static final String pg_type_bigserial = "bigserial";
	public static final String pg_type_bigint = "bigint";
	public static final String pg_type_integer = "integer";
	public static final String pg_type_smallint = "smallint";
	public static final String pg_type_varchar = "varchar";
	public static final String pg_type_text = "text";
	public static final String pg_type_timestamp = "timestamp";
	public static final String pg_type_date = "date";
	public static final String pg_type_bool = "bool";

	public CodeStatement(String sentence) {
		List<Field> lstField = null;
		if (sentence != null && !sentence.isBlank()) {
			lstField = new ArrayList<>();
			String[] lstSentenceLine = sentence.split(",");
			for (int i = 0; i < lstSentenceLine.length; i++) {
				if (!lstSentenceLine[i].isBlank()) {
					List<String> lstPart = this.getLstPartFromSentence(lstSentenceLine[i]);
					Field field = new Field();
					boolean bNot = false;
					boolean bNull = false;
					for (int j = 0; j < lstPart.size(); j++) {
						String part = lstPart.get(j).toLowerCase();

						if (j == 0) {
							field.setSqlName(part);
						} else if (part.contains(pg_type_numeric)) {
							field.setSqlType(pg_type_numeric);
							field.setJavaType("BigDecimal");
							field.setRsGetType("rs.getBigDecimal");
							field.setPsSetType("ps.setString");
						} else if (Objects.equals(part, pg_type_bigserial)) {
							field.setSqlType(pg_type_bigserial);
							field.setJavaType("Long");
							field.setRsGetType("rs.getLong");
							field.setPsSetType("ps.setLong");
						} else if (Objects.equals(part, pg_type_bigint)) {
							field.setSqlType(pg_type_bigint);
							field.setJavaType("Long");
							field.setRsGetType("rs.getLong");
							field.setPsSetType("ps.setLong");
						} else if (Objects.equals(part, pg_type_integer)) {
							field.setSqlType(pg_type_integer);
							field.setJavaType("Integer");
							field.setRsGetType("rs.getInt");
							field.setPsSetType("ps.setInt");
						} else if (Objects.equals(part, pg_type_smallint)) {
							field.setSqlType(pg_type_smallint);
							field.setJavaType("Short");
							field.setRsGetType("rs.getShort");
							field.setPsSetType("ps.setShort");
						} else if (part.contains(pg_type_varchar)) {
							field.setSqlType(pg_type_varchar);
							field.setJavaType("String");
							field.setRsGetType("rs.getString");
							field.setPsSetType("ps.setString");
						} else if (part.contains(pg_type_text)) {
							field.setSqlType(pg_type_text);
							field.setJavaType("String");
							field.setRsGetType("rs.getString");
							field.setPsSetType("ps.setString");
						} else if (Objects.equals(part, pg_type_timestamp)) {
							field.setSqlType(pg_type_timestamp);
							field.setJavaType("Date");
							field.setRsGetType("rs.getTimestamp");
							field.setPsSetType("ps.setTimestamp");
						} else if (Objects.equals(part, pg_type_date)) {
							field.setSqlType(pg_type_timestamp);
							field.setJavaType("Date");
							field.setRsGetType("rs.getDate");
							field.setPsSetType("ps.setDate");
						} else if (Objects.equals(part, pg_type_bool)) {
							field.setSqlType(pg_type_bool);
							field.setJavaType("Boolean");
							field.setRsGetType("rs.getBoolean");
							field.setPsSetType("ps.setBoolean");
						} else if (Objects.equals(part, "not")) {
							bNot = true;
						} else if (Objects.equals(part, "null")) {
							bNull = true;
						}
					}
					field.setNotNull(bNot && bNull);
					lstField.add(field);
				}

			}

		}

		this.lstField = lstField;
	}

	private List<String> getLstPartFromSentence(String sentenceLine) {
		List<String> lstPart = null;
		if (sentenceLine != null && !sentenceLine.isBlank()) {
			lstPart = new ArrayList<>();
			sentenceLine = this.normalizeSentences(sentenceLine);
			String[] parts = sentenceLine.split(" ");
			for (String part : parts) {
				part = part.trim();
				if (part != "" && !part.isBlank()) {
					lstPart.add(part);
				}
			}

		} else {
			throw new RuntimeException("Sentence should not be empty.");
		}
		return lstPart;
	}

	private String normalizeSentences(String def) {
		String response = "";
		def = def.replaceAll("\t", " ");

		char[] array = def.toCharArray();
		for (int i = 0; i < array.length; i++) {
			char c1 = array[i];
			char c2 = 0;
			if (array.length > i + 1) {
				c2 = array[i + 1];
			}

			char cSpace = ' ';
			char cNull = 0;

			if ((c1 == cSpace || c1 == cNull) && (c2 == cSpace || c2 == cNull)) {
				continue;
			} else if ((c1 != cSpace || c1 != cNull) && (c2 != cSpace || c2 != cNull)) {
				response += c1;
			} else if ((c1 != cSpace || c1 != cNull)) {
				response += c1;
			}
		}
		return response;
	}

	public List<String> getLstDTOFields() {
		List<String> lstResponse = null;
		if (this.lstField != null) {
			lstResponse = new ArrayList<>();
			for (Field field : lstField) {
				String dtoElement = "private " + field.getJavaType() + " " + field.getJavaName() + ";";
				lstResponse.add(dtoElement);
			}
		}
		return lstResponse;
	}

	public List<String> getLstPS(String prefix) {
		List<String> lstResponse = null;
		lstResponse = new ArrayList<>();
		
		int pos = 1;
		for (Field field : lstField) {
			String dtoElement = "";
			
			if (Objects.equals(field.getSqlType(), pg_type_timestamp)) {
				dtoElement = this.getPsOfPgTimestamp(field, prefix, pos);
			} else if (Objects.equals(field.getSqlType(), pg_type_date)) {
				dtoElement = this.getPsOfPgDate(field, prefix, pos);
			} else if (Objects.equals(field.getSqlType(), pg_type_bigserial)) {
				dtoElement = this.getPsOfPgBigint(field, prefix, pos);
			} else if (Objects.equals(field.getSqlType(), pg_type_bigint)) {
				dtoElement = this.getPsOfPgBigint(field, prefix, pos);
			} else if (Objects.equals(field.getSqlType(), pg_type_integer)) {
				dtoElement = this.getPsOfPgInteger(field, prefix, pos);
			} else if (Objects.equals(field.getSqlType(), pg_type_numeric)) {
				dtoElement = this.getPsOfPgNumeric(field, prefix, pos);
			}else if (Objects.equals(field.getSqlType(), pg_type_smallint)) {
				dtoElement = this.getPsOfPgSmallint(field, prefix, pos);
			} else if (Objects.equals(field.getSqlType(), pg_type_text)) {
				dtoElement = this.getPsOfPgVarchar(field, prefix, pos);
			} else if (Objects.equals(field.getSqlType(), pg_type_varchar)) {
				dtoElement = this.getPsOfPgVarchar(field, prefix, pos);
			} else if (Objects.equals(field.getSqlType(), pg_type_bool)) {
				dtoElement = this.getPsOfPgBoolean(field, prefix, pos);
			} else {
				//throw new RuntimeException("El tipo de campo no existe: " + field.getSqlType());
				System.out.println("El tipo de campo no existe: " + field.getSqlType());
			}
			lstResponse.add(dtoElement);
			++pos;
		}

		return lstResponse;
	}

	private String getPsOfPgTimestamp(Field field, String prefix, Integer pos) {
		String response = null;
		/** PREFIX LOGIC **/
		if (prefix != null) {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + ", new java.sql.Timestamp(" + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "().getTime())" + ");";

			}
			// null logic
			else {
				response = "" + "if(" + prefix + ".get" + this.capitalizeFirstChar(field.getJavaName())
						+ "() ==  null ){ \n" + "	ps.setNull(" + pos + ",java.sql.Types.TIMESTAMP);" + "\n} else { \n"
						+ "	" + field.getPsSetType() + "(" + pos + ", new java.sql.Timestamp(" + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "().getTime())" + ");" + "\n}";
			}
		}
		/** NO PREFIX LOGIC **/
		else {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + ", new java.sql.Timestamp(" + field.getJavaName()
						+ ".getTime())" + ");";
			}
			// null logic
			else {
				response = "" + "if(" + field.getJavaName() + " ==  null){ \n" + "	ps.setNull(" + pos
						+ ",java.sql.Types.TIMESTAMP);" + "\n} else { \n" + "	" + field.getPsSetType() + "(" + pos
						+ ", new java.sql.Timestamp(" + field.getJavaName() + "())" + ");" + "\n}";

			}
		}

		return response;
	}

	private String getPsOfPgDate(Field field, String prefix, Integer pos) {
		String response = null;
		/** PREFIX LOGIC **/
		if (prefix != null) {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + ", new java.sql.Date(" + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "().getTime())" + ");";

			}
			// null logic
			else {
				response = "" + "if(" + prefix + ".get" + this.capitalizeFirstChar(field.getJavaName())
						+ "() ==  null){ \n" + "	ps.setNull(" + pos + ",java.sql.Types.DATE);" + "\n} else { \n"
						+ "	" + field.getPsSetType() + "(" + pos + ", new java.sql.Date(" + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "().getTime())" + ");" + "\n}";
			}
		}
		/** NO PREFIX LOGIC **/
		else {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + ", new java.sql.Timestamp(" + field.getJavaName() + "."
						+ "getTime())" + ");";
			}
			// null logic
			else {
				response = "" + "if(" + field.getJavaName() + " ==  null){ \n" + "	ps.setNull(" + pos
						+ ",java.sql.Types.TIMESTAMP);" + "\n} else { \n" + "	" + field.getPsSetType() + "(" + pos
						+ ", new java.sql.Timestamp(" + field.getJavaName() + "().getTime())" + ");" + "\n}";

			}
		}

		return response;
	}

	private String getPsOfPgBigint(Field field, String prefix, Integer pos) {
		String response = null;
		/** PREFIX LOGIC **/
		if (prefix != null) {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");";

			}
			// null logic
			else {
				response = "" + "if(" + prefix + ".get" + this.capitalizeFirstChar(field.getJavaName())
						+ "() ==  null){ \n" + "	ps.setNull(" + pos + ",java.sql.Types.BIGINT);" + "\n} else { \n"
						+ "	" + field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");" + "\n}";
			}
		}
		/** NO PREFIX LOGIC **/
		else {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + field.getJavaName() + ");";
			}
			// null logic
			else {
				response = "" + "if(" + field.getJavaName() + " ==  null){ \n"
						+ "	ps.setNull(" + pos + ",java.sql.Types.BIGINT);" + "\n} else { \n" + "	"
						+ field.getPsSetType() + "(" + pos + "," + field.getJavaName() + "()" + ");" + "\n}";

			}
		}

		return response;
	}
	
	
	private String getPsOfPgNumeric(Field field, String prefix, Integer pos) {
		String response = null;
		/** PREFIX LOGIC **/
		if (prefix != null) {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");";

			}
			
			// null logic
			else {
				response = "" + "if(" + prefix + ".get" + this.capitalizeFirstChar(field.getJavaName())
						+ "() ==  null){ \n" + "	ps.setNull(" + pos + ",java.sql.Types.NUMERIC);" + "\n} else { \n"
						+ "	" + field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");" + "\n}";
			}
		}
		/** NO PREFIX LOGIC **/
		else {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + field.getJavaName() + ");";
			}
			// null logic
			else {
				response = "" + "if(" + field.getJavaName() + " ==  null){ \n"
						+ "	ps.setNull(" + pos + ",java.sql.Types.BIGINT);" + "\n} else { \n" + "	"
						+ field.getPsSetType() + "(" + pos + "," + field.getJavaName() + "()" + ");" + "\n}";

			}
		}

		return response;
	}

	private String getPsOfPgInteger(Field field, String prefix, Integer pos) {
		String response = null;
		/** PREFIX LOGIC **/
		if (prefix != null) {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");";

			}
			// null logic
			else {
				response = "" + "if(" + prefix + ".get" + this.capitalizeFirstChar(field.getJavaName())
						+ "() ==  null){ \n" + "	ps.setNull(" + pos + ",java.sql.Types.INTEGER);" + "\n} else { \n"
						+ "	" + field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");" + "\n}";
			}
		}
		/** NO PREFIX LOGIC **/
		else {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + field.getJavaName() + ");";
			}
			// null logic
			else {
				response = "" + "if(" + field.getJavaName() + " ==  null){ \n" + "	ps.setNull(" + pos
						+ ",java.sql.Types.INTEGER);" + "\n} else { \n" + "	" + field.getPsSetType() + "(" + pos + ","
						+ field.getJavaName() + "()" + ");" + "\n}";

			}
		}

		return response;
	}

	private String getPsOfPgSmallint(Field field, String prefix, Integer pos) {
		String response = null;
		/** PREFIX LOGIC **/
		if (prefix != null) {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");";

			}
			// null logic
			else {
				response = "" + "if(" + prefix + ".get" + this.capitalizeFirstChar(field.getJavaName())
						+ "() ==  null){ \n" + "	ps.setNull(" + pos + ",java.sql.Types.SMALLINT);" + "\n} else { \n"
						+ "	" + field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");" + "\n}";
			}
		}
		/** NO PREFIX LOGIC **/
		else {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + field.getJavaName() + ");";
			}
			// null logic
			else {
				response = "" + "if(" + field.getJavaName() + " ==  null){ \n" + "	ps.setNull(" + pos
						+ ",java.sql.Types.SMALLINT);" + "\n} else{ \n" + "	" + field.getPsSetType() + "(" + pos + ","
						+ field.getJavaName() + "()" + ");" + "\n}";

			}
		}

		return response;
	}

	private String getPsOfPgVarchar(Field field, String prefix, Integer pos) {
		String response = null;
		/** PREFIX LOGIC **/
		if (prefix != null) {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");";

			}
			// null logic
			else {
				response = "" + "if(" + prefix + ".get" + this.capitalizeFirstChar(field.getJavaName())
						+ "() ==  null){ \n" + "	ps.setNull(" + pos + ",java.sql.Types.VARCHAR);" + "\n} else { \n"
						+ "	" + field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");" + "\n}";
			}
		}
		/** NO PREFIX LOGIC **/
		else {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + field.getJavaName() + ");";
			}
			// null logic
			else {
				response = "" + "if(" + field.getJavaName() + " ==  null){ \n" + "	ps.setNull(" + pos
						+ ",java.sql.Types.VARCHAR);" + "\n} else{ \n" + "	" + field.getPsSetType() + "(" + pos + ","
						+ field.getJavaName() + "()" + ");" + "\n}";

			}
		}

		return response;
	}

	private String getPsOfPgBoolean(Field field, String prefix, Integer pos) {
		String response = null;
		/** PREFIX LOGIC **/
		if (prefix != null) {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");";

			}
			// null logic
			else {
				response = "" + "if(" + prefix + ".get" + this.capitalizeFirstChar(field.getJavaName())
						+ " ==  null){ \n" + "	ps.setNull("+pos+",java.sql.Types.BOOLEAN);" + "\n} else { \n" + "	"
						+ field.getPsSetType() + "(" + pos + "," + prefix + ".get"
						+ this.capitalizeFirstChar(field.getJavaName()) + "()" + ");" + "\n}";
			}
		}
		/** NO PREFIX LOGIC **/
		else {
			// not null logic
			if (field.getNotNull()) {
				response = field.getPsSetType() + "(" + pos + "," + field.getJavaName() + ");";
			}
			// null logic
			else {
				response = "" + "if(" + field.getJavaName() + " ==  null){ \n" + "	ps.setNull(" + pos
						+ ",java.sql.Types.BOOLEAN);" + "\n} else{ \n" + "	" + field.getPsSetType() + "(" + pos + ","
						+ field.getJavaName() + "()" + ");" + "\n}";

			}
		}

		return response;
	}
	// dto.setNombreContacto(
	//	rs.getString("nombre_contacto")
	//);
	public List<String> getLstRS(String prefix) {
		List<String> lstResponse = null;
		if(prefix == null) {
			lstResponse = new ArrayList<>();
			for (Field field : lstField) {
				String dtoElement = field.getJavaType() + "  " + field.getJavaName() + " = " + field.getRsGetType() + "(\""
						+ field.getSqlName() + "\");";
				lstResponse.add(dtoElement);
			}
		} else {
			lstResponse = new ArrayList<>();
			for (Field field : lstField) {
				String dtoElement = 
						prefix + ".set" + this.capitalizeFirstChar(field.getJavaName()) + "("
						+ field.getRsGetType() +"(\""+field.getSqlName()+"\")"  
						+ ");";
				lstResponse.add(dtoElement);
			}						
		}

		return lstResponse;
		
	}

	private String capitalizeFirstChar(String txt) {
		return txt.substring(0, 1).toUpperCase() + txt.substring(1);
	}

	public List<Field> getLstField() {
		return lstField;
	}

}
