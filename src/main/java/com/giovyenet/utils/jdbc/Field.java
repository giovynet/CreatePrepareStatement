package com.giovyenet.utils.jdbc;

public class Field {
	
	private String javaName;
	private String sqlName;
	private String javaType;
	private String sqlType;
	private String rsGetType;
	private String psSetType;
	private Boolean notNull;

	
	public String getJavaName() {
		this.javaName = null;
		if(this.sqlName != null) {
			this.javaName = 
					this.sqlName
					.replace("_a","A")
					.replace("_b","B")
					.replace("_c","C")
					.replace("_d","D")
					.replace("_e","E")
					.replace("_f","F")
					.replace("_g","G")
					.replace("_h","H")
					.replace("_i","I")
					.replace("_j","J")
					.replace("_k","L")
					.replace("_l","L")
					.replace("_m","M")
					.replace("_n","N")
					.replace("_ñ","Ñ")
					.replace("_o","O")
					.replace("_p","P")
					.replace("_q","Q")
					.replace("_r","R")
					.replace("_s","S")
					.replace("_t","T")
					.replace("_u","U")
					.replace("_v","V")
					.replace("_w","W")
					.replace("_x","X")
					.replace("_y","Y")
					.replace("_z","Z")
					
					.replace("_A","A")
					.replace("_B","B")
					.replace("_C","C")
					.replace("_D","D")
					.replace("_E","E")
					.replace("_F","F")
					.replace("_G","G")
					.replace("_H","H")
					.replace("_I","I")
					.replace("_J","J")
					.replace("_K","L")
					.replace("_L","L")
					.replace("_M","M")
					.replace("_N","N")
					.replace("_Ñ","Ñ")
					.replace("_O","O")
					.replace("_P","P")
					.replace("_Q","Q")
					.replace("_R","R")
					.replace("_S","S")
					.replace("_T","T")
					.replace("_U","U")
					.replace("_V","V")
					.replace("_W","W")
					.replace("_X","X")
					.replace("_Y","Y")
					.replace("_Z","Z")
					
					.replace("_0","0")
					.replace("_1","1")
					.replace("_2","2")
					.replace("_3","3")
					.replace("_4","4")
					.replace("_5","5")
					.replace("_6","6")
					.replace("_7","7")
					.replace("_8","8")
					.replace("_9","9");
		}
		return javaName;
	}

	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String getSqlType() {
		return sqlType;
	}
	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public Boolean getNotNull() {
		return notNull;
	}
	public void setNotNull(Boolean notNull) {
		this.notNull = notNull;
	}

	public String getRsGetType() {
		return rsGetType;
	}

	public void setRsGetType(String rsGetType) {
		this.rsGetType = rsGetType;
	}

	public String getPsSetType() {
		return psSetType;
	}

	public void setPsSetType(String psSetType) {
		this.psSetType = psSetType;
	}
	
	
}