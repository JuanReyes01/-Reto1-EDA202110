package view;

import model.data_structures.ILista;
import model.logic.Categoria;
import model.logic.Modelo;
import model.logic.YoutubeVideo;

public class View 
{
	
	    /**
	     * Metodo constructor
	     */
	    public View()
	    {
	    }
	    
		public void printMenu()
		{
			System.out.println("1. Inicializar estructuras de datos");
			System.out.println("2. Cargar datos del archivo CSV");
			System.out.println("3. Obtener una muestra de los videos");
			System.out.println("4. Organizar el arreglo");
			System.out.println("5. Exit");
			System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
		}

		public void printMessage(String mensaje) {

			System.out.println(mensaje);
		}		
		
		public void printModelo(Modelo modelo)
		{
			String a="[";
			for(int i=1; i<=modelo.darTamano();i++)
				a+=modelo.darArreglo().getElement(i)+" ";
			a = a.trim();
			a+="]";
			System.out.println(a);
		
		}
		
		public void printPrimerosYUltimos(ILista<YoutubeVideo> l)
		{
			System.out.println("===================================================================================");
			System.out.println("Primeros 10 elementos:");
			for(int i=1; i<=10; i++){
				System.out.println(l.getElement(i).darTitulo()+": "+l.getElement(i).darLikes());
			}
			System.out.println("===================================================================================");
			System.out.println("Ultimos 10 elementos:");
			for(int i=l.size();i>=(l.size()-10);i--){
				System.out.println(l.getElement(i).darTitulo()+": "+l.getElement(i).darLikes());
			}
			System.out.println("===================================================================================");
		}
		
		public void printCategorias(Modelo modelo){
			ILista<Categoria> l = modelo.darCategorias();
			for(int i=1; i<=l.size();i++){
				System.out.println(l.getElement(i).darNombre()+": "+l.getElement(i).darId());
			}
		}
}
