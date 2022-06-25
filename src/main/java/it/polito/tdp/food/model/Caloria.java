package it.polito.tdp.food.model;

public class Caloria implements Comparable<Caloria> {

	Food f;
	Double numero;
	public Caloria(Food f, Double numero) {
		super();
		this.f = f;
		this.numero = numero;
	}
	public Food getF() {
		return f;
	}
	public void setF(Food f) {
		this.f = f;
	}
	public Double getNumero() {
		return numero;
	}
	public void setNumero(Double numero) {
		this.numero = numero;
	}
	@Override
	public String toString() {
		return f.getDisplay_name()+" - "+this.numero;
	}
	@Override
	public int compareTo(Caloria o) {
		return -(this.getNumero().compareTo(o.getNumero())) ;
	}
	
	
	
	
}
