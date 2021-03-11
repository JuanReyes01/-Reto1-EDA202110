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
	private static final String VIDEO = "./data/videos-all.csv";
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
		Reader in = new FileReader(VIDEO);
		
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
				Categoria nueva =  new Categoria(Integer.parseInt(x[0]), x[1].trim());
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


	public ArregloDinamico<YoutubeVideo> sublistaR1(ILista<YoutubeVideo> l, int c,String pais){
		ArregloDinamico<YoutubeVideo> nueva = new ArregloDinamico<YoutubeVideo>();
		for(int i=1; i<=l.size(); i++){
			if((l.getElement(i).darId_categoria()==c)&&(l.getElement(i).darPais().trim().compareToIgnoreCase((pais))==0))
				nueva.addLast(l.getElement(i)); 
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
			System.out.println(actual.darNombre().compareToIgnoreCase(categoria));
			if(actual.darNombre().compareToIgnoreCase(categoria)==0){
				c = actual.darId();
				stop = true;
			}
		}
		if(c!=0){
		//Arreglo con la lista de paises y categoria O(N)
		ArregloDinamico<YoutubeVideo> p= sublistaR1(datos,c,pais);
		
		Comparator<YoutubeVideo> comp = new YoutubeVideo.ComparadorXViews();
		//Merge sort O(NlogN)
		o.ordenarQuickSort(p, comp, false);
		
		//Resultado final
		p = p.sublista(num);
		return p;
		}
		return null;
		
	}

	
	/**
	 * Busca el video que ha sido mas tendencia en un determinado pais.
	 * @param pais Pais donde son tendencia los videos. pais != null.
	 * @return Como respuesta deben aparecer el video con mayor tendencia en el pais.  
	 */
    public String req2(String pais){
    	//Lista de videos individuales
		ArregloDinamico<YoutubeVideo> repetidos = new ArregloDinamico<YoutubeVideo>();
		//Lista de videos del pais
		ArregloDinamico<YoutubeVideo> listaPais = new ArregloDinamico<YoutubeVideo>();
		int maximo = 0;
		YoutubeVideo mayor = null;
		//Acotar la lista grande a solo la categoria que nos interesa
		for(int i=1; i<=datos.size(); i++){
			YoutubeVideo actual = datos.getElement(i);
			if(actual.darPais().trim().compareToIgnoreCase(pais)==0){
				listaPais.addLast(actual);
			}	
		}
		
		int i= 1;
		while(i<=listaPais.size()){
			YoutubeVideo actual = listaPais.getElement(i);
			repetidos.addLast(actual);
			int eliminados = 1;
			for(int j=i+1;j<=listaPais.size();j++){
				if(actual.darVideoID().equals(listaPais.getElement(j).darVideoID())){
					listaPais.deleteElement(j);
					eliminados++;
				}
			}
			listaPais.deleteElement(i);
			if(eliminados>maximo){
				maximo = eliminados;
				mayor = actual;
			}
			i++;
		}
		return (mayor!=null)?" Titulo: "+mayor.darTitulo()+"\n Chanel_Title: "+mayor.darCanal()+"\n country: "+mayor.darPais()+" \n Dias: "+maximo:"";
		
	}
    
    /**
	 * Busca el video que ha sido mas tendencia en una categoria especifica.
	 * @param categoria Categoria especifica en la que estan los videos.
	 * @return Como respuesta deben aparecer el video con mayor tendencia de la categoria.  
	 */
	public String req3 (String categoria){
		int x = 0;
		boolean z = false;
		for(int i=1; i<=categorias.size()&&!z;i++){
			Categoria actual = categorias.getElement(i);
			if(actual.darNombre().compareToIgnoreCase(categoria)==0){
				x = actual.darId();
				z = true;
			}
		}
		
		ArregloDinamico<YoutubeVideo> listaCategoria = new ArregloDinamico<>();
		for(int i=1;i<=datos.size();i++){
			if(datos.getElement(i).darId_categoria()==x){
				listaCategoria.addLast(datos.getElement(i));
			}
		}
		int maximo = 0;
		YoutubeVideo mayor = null;
		int i = 1;
		while(i<=listaCategoria.size()){
			YoutubeVideo actual = listaCategoria.getElement(i);
			int eliminados = 1;
			for(int j=i+1;j<=listaCategoria.size();j++){
				if(actual.darVideoID().equals(listaCategoria.getElement(j).darVideoID())){
					listaCategoria.deleteElement(j);
					eliminados++;
				}
			}
			listaCategoria.deleteElement(i);
			if(eliminados>maximo){
				maximo = eliminados;
				mayor = actual;
			}
			i++;
		}
		return (mayor!=null)?" \n Title: "+mayor.darTitulo()+"\n channel_title: "+mayor.darCanal()+"\n category_Id: "+mayor.darId_categoria()+"\n Dias:"+maximo:"";
	}
	
	
	//Metodo obsoleto 
	public ArregloDinamico<String> tags(YoutubeVideo y){
		String[] tag = y.darTags().split("\\|");
		ArregloDinamico<String> tags = new ArregloDinamico<String>();
		for(int i=0; i<tag.length;i++){
			//System.out.println(tag[i].replace("\"", ""));
			tags.addLast(tag[i].replace("\"", "").trim());
		}
		return tags;
	}
	
	public ArregloDinamico<YoutubeVideo> sublistaR4(ILista<YoutubeVideo> l, String tag, String pais){
		ArregloDinamico<YoutubeVideo> nuevo = new ArregloDinamico<YoutubeVideo>();
		for(int i=1;i<=l.size();i++){
			YoutubeVideo a = l.getElement(i);
			if((a.darPais().trim().compareToIgnoreCase(pais)==0&&a.darTags().contains(tag)))
					nuevo.addLast(a);
		}
		return nuevo;
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
		
		ArregloDinamico<YoutubeVideo> sub = sublistaR4(datos, etiqueta, pais);
		Comparator<YoutubeVideo> comp = new YoutubeVideo.ComparadorXLikes();		
		sub = (ArregloDinamico<YoutubeVideo>) o.ordenarQuickSort(sub, comp, false);
		
		return sub.sublista(num);
	}
	

}


		