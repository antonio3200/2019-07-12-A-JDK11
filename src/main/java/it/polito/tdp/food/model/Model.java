package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private FoodDao dao;
	private SimpleWeightedGraph<Food,DefaultWeightedEdge> grafo;
	private List<Food> vertici;
	private Map<Integer,Food> idMap;
	
	
	public Model() {
	
		this.dao=new FoodDao();
		this.vertici= new ArrayList<>();
		this.idMap= new HashMap<>();
	}
	
	public void creaGrafo(int porzioni) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.vertici=this.dao.getFoodByPorzioni(porzioni,this.idMap);
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, this.vertici);
		//aggiungo archi
		List<Arco> archi= this.dao.getArco(idMap);
		for(Arco a: archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getF1(), a.getF2(), a.getPeso());
		}
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Food> getVertici() {
		List<Food> result= this.vertici;
		Collections.sort(result);
		return result;
	}
	
	public List<Caloria> getCibiMigliori(Food selezionato){
		List<Caloria> result=new ArrayList<>();
		List<Food> vicini= Graphs.neighborListOf(this.grafo,selezionato);
		
		for(Food f : vicini) {
			Caloria c = new Caloria(f,this.grafo.getEdgeWeight(this.grafo.getEdge(selezionato, f)));
			result.add(c);
		}
		Collections.sort(result);
		return result;
	}
	
}
