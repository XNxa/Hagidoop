package interfaces;

import java.io.Serializable;

public class KV implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	public static final String SEPARATOR = "<->";
	public static final String INTERNAL_SEPARTOR = "::";
	
	public String k;
	public String v;
	
	public KV() {}
	
	public KV(String k, String v) {
		super();
		this.k = k;
		this.v = v;
	}

	public String toString() {
		return  k + INTERNAL_SEPARTOR + v ;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
