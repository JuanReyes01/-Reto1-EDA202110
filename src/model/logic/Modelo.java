package model.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import model.data_structures.ArregloDinamico;
import model.data_structures.IArregloDinamico;
import model.data_structures.ILista;
import model.data_structures.ListaEncadenada;


import model.utils.Ordenamiento;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	/**
	 * Atributos del modelo del mundo
	 */
	private ILista<Categoria> categorias;
	private ILista<YoutubeVideo> datos;
	private Ordenamiento<YoutubeVideo> o;
	public Modelo()
	{
		datos = new ArregloDinamico<YoutubeVideo>();
		categorias = new ArregloDinamico<Categoria>();
		o = new Ordenamiento<YoutubeVideo>();
	}	
	
	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo 
	 * @return numero de elementos presentes en el modelo
	 */
	public int darTamano()
	{
		return datos.size();
	}
	

	/**
	 * Requerimiento de agregar dato
	 * @param dato
	 */
	public void agregar(YoutubeVideo dato)
	{	
		datos.addLast(dato);
	}
	
	/**
	 * Requerimiento buscar dato
	 * @param dato Dato a buscar
	 * @return dato encontrado
	 */
	public Object buscar(YoutubeVideo dato)
	{
		return datos.getElement(datos.isPresent(dato));
	}
	
	/**
	 * Requerimiento eliminar dato
	 * @param <T>
	 * @param dato Dato a eliminar
	 * @return dato eliminado
	 */
	public YoutubeVideo eliminar(YoutubeVideo dato)
	{
		return datos.deleteElement(datos.isPresent(dato));
	}
	
	public ILista<YoutubeVideo> subLista(int i){
		return datos.sublista(i);
	}
	
	public ILista<YoutubeVideo> darArreglo(){
		return datos;
	}
	
	public String cargarDatos() throws IOException, ParseException{
		long miliI = System.currentTimeMillis();
		Reader in = new FileReader("./data/videos-small.csv");
		
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);	
		for (CSVRecord record : records) {
		    String id = record.get(0);
		    String trending = record.get(1);
		    String titulo = record.get(2);
		    String canal = record.get(3);
		    String YoutubeVideo = record.get(4);
		    String fechaP = record.get(5);
		    String tags = record.get(6);
		    String vistas = record.get(7);
		    String likes  = record.get(8);
		    String dislikes = record.get(9);
		    String coment = record.get(10);
		    String foto = record.get(11);
		    String nComent = record.get(12);
		    String rating = record.get(13);
		    String vidErr = record.get(14);
		    String descripcion = record.get(15);
		    String pais = record.get(16);
		    //--------------------------------------------------------------------
		    if(!id.equals("video_id")){
		    SimpleDateFormat formato = new SimpleDateFormat("yyy/MM/dd");
		    String[] aux = trending.split("\\.");
		    Date fechaT = formato.parse(aux[0]+"/"+aux[2]+"/"+aux[1]);
		    SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");		   
		    Date fechaPu = formato2.parse(fechaP);
		    YoutubeVideo nuevo = new YoutubeVideo(id, fechaT, titulo, canal, Integer.parseInt(YoutubeVideo), fechaPu, tags, Integer.parseInt(vistas), Integer.parseInt(likes), Integer.parseInt(dislikes), Integer.parseInt(coment), foto, (nComent.equals("FALSE")?false:true), (rating.equals("FALSE")?false:true), (vidErr.equals("FALSE")?false:true), descripcion, pais);
		    agregar(nuevo);
		    }
		}
		long miliF = System.currentTimeMillis();
		return "Tiempo de ejecución total: "+((miliF-miliI))+" milisegundos, \nTotal datos cargados: "+ datos.size();
	}

	public void cargarId() throws IOException, FileNotFoundException{
		Reader in = new FileReader("./data/category-id.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);	
		for (CSVRecord record : records) {
			String n = record.get(0);
			if(!n.equals("id	name")){
				String[] x = n.split("	 ");
				Categoria nueva =  new Categoria(Integer.parseInt(x[0]), x[1]);
				agregarCategoria(nueva);
			}
		}
		
	}
	
	public ILista<Categoria> darCategorias(){
		return categorias;
	}
	
	public void agregarCategoria(Categoria elem){
		categorias.addLast(elem);
	}
	
	/**
	 * Metodo que sobreescribe la busqueda que realiza arreglo dinamico con una busqueda binaria
	 * Esto es posible porque la lista de categorias esta ordenada desde que se carga 
	 */
	public Categoria buscarCategoriaBin(int pos){
		int i = 1;
		int f = categorias.size();
		int elem = -1;
		boolean encontro = false;
		while ( i <= f && !encontro )
		{
		int m = (i + f) / 2;
		if ( categorias.getElement(m).darId() == pos )
		{
		elem = m;
		encontro = true;
		}
		else if ( categorias.getElement(m).darId() > pos )
		{
		f = m - 1;
		}
		else
		{
		i = m + 1;
		}
		}
		return categorias.getElement(elem);
	}

	/**
	 * Metodo que realiza una sublista de paises
	 * @param Pais del cual se desea realizar la sublisa, Pais!=null, Pais!=""
	 * @return ILista<YoutubeVideo> 
	 */
	public ArregloDinamico<YoutubeVideo> sublistaPais(String pais){
		ArregloDinamico<YoutubeVideo> nueva = new ArregloDinamico<YoutubeVideo>();
		for(int i=1; i<=datos.size(); i++){
			if(datos.getElement(i).darPais().compareToIgnoreCase((pais))==0)
				nueva.addLast(datos.getElement(i)); 
		}
		return nueva;
	}


	/**
	 * Busca los n videos con mas views que son tendencia en un determinado pais, dada una categoria especifica.
	 * @param pais Pais donde son tendencia los videos. pais != null
	 * @param num Numero de videos que se desean ver. num > 0
	 * @param categoria Categoria especifica en la que estan los videos.
	 * @return Como respuesta deben aparecer los n videos que cumplen las caracteristicas y su respectiva informacion.  
	 */

	public ILista<YoutubeVideo> req1(String pais, int num, String categoria){
		int c = 0;
		boolean stop = false;
		//Determinar el id de la categoria O(N) 
		for(int i=1; i<=categorias.size()&&!stop;i++){
			Categoria actual = categorias.getElement(i);
			if(actual.darNombre().compareTo(categoria)==0){
				c = actual.darId();
				stop = true;
			}
		}
		
		//Arreglo con la lista de paises
		ArregloDinamico<YoutubeVideo> p= sublistaPais(pais);
		Comparator<YoutubeVideo> comp = new YoutubeVideo.ComparadorXViews();
		o.ordenarMerge(p, comp, false);
		
		//Resultado final
		p = p.sublista(c);
		return p;
		
		
	}

	
	/**
	 * Busca el video que ha sido mas tendencia en un determinado pais.
	 * @param pais Pais donde son tendencia los videos. pais != null.
	 * @return Como respuesta deben aparecer el video con mayor tendencia en el pais.  
	 */
    public ILista<YoutubeVideo> req2 (String pais){
		int x = 0;
				ArregloDinamico<YoutubeVideo> p = sublistaPais(pais);
				for(int i=1; i<=p.size();i++){
					for(int j=i+1; j<=p.size();j++){
						
					}
				}
				Comparator<YoutubeVideo> comp = new YoutubeVideo.ComparadorXViews();
				o.ordenarMerge(p, comp, false);
				p = p.sublista(x);
		
		return p;
		
	}
    
    /**
	 * Busca el video que ha sido mas tendencia en una categoria especifica.
	 * @param categoria Categoria especifica en la que estan los videos.
	 * @return Como respuesta deben aparecer el video con mayor tendencia de la categoria.  
	 */
	public ILista<YoutubeVideo> req3 (String categoria){
		int x = 0;
		boolean z = false;
		for(int i=1; i<=categorias.size()&&!z;i++){
			Categoria actual = categorias.getElement(i);
			if(actual.darNombre().compareTo(categoria)==0){
				x = actual.darId();
				z = true;
			}
		}
				
				ArregloDinamico<YoutubeVideo> p = (ArregloDinamico<YoutubeVideo>) subLista(1);
				
				Comparator<YoutubeVideo> comp = new YoutubeVideo.ComparadorXViews();
				o.ordenarMerge(p, comp, false);
				//p = p.sublista(x);
				return p;
		
	}
	
	/**
	 * Busca los n videos con mas views que son tendencia en un determinado pais y que posean la etiqueta designada.
	 * @param pais Pais donde son tendencia los videos. pais != null
	 * @param num Numero de videos que se desean ver. num > 0
	 * @param etiqueta Tag especifica que tienen los videos. != " " y != null
	 * @return Como respuesta deben aparecer los n videos que cumplen las caracteristicas y su respectiva informacion.  
	 * @throws FileNotFoundException 
	 */
	public ILista<YoutubeVideo> req4(String pais, int num, String etiqueta) {
		
		int c = 0;
		

	    String[] x = null;
	
	    for(int i = 0; i < x.length; i++ ){
		if(x[i].toLowerCase().contains(etiqueta.toLowerCase())){
			
		}
		}
		
		ArregloDinamico<YoutubeVideo> p= sublistaPais(pais);
		Comparator<YoutubeVideo> comp = new YoutubeVideo.ComparadorXViews();
		o.ordenarMerge(p, comp, false);

		p = p.sublista(c);
		return p;
		
	}
	

}


		