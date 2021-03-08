package controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import model.data_structures.ILista;
import model.logic.Modelo;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;
	
	/* Instancia de la Vista*/
	private View view;
	
	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new View();
		modelo = new Modelo();
	}


	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		String dato = "";
		Object respuesta = null;

		
		while(!fin){
			view.printMenu();
			
			int option = lector.nextInt();
			switch(option){
				case 1:
					view.printMessage("Cargando datos en el sistema...");
					String r;
				try {
					r = modelo.cargarDatos();
					view.printMessage("------------------------------------------");
					view.printMessage(r);
					view.printMessage("Primer video: \n titulo: "+modelo.darArreglo().firstElement().darTitulo()
							+" \n Canal: "+modelo.darArreglo().firstElement().darCanal()
							+" \n fecha trending: "+modelo.darArreglo().firstElement().darFechaT()
							+" \n país: "+modelo.darArreglo().firstElement().darPais()
							+" \n Visitas: "+modelo.darArreglo().firstElement().darViews()
							+" \n Likes: "+modelo.darArreglo().firstElement().darLikes()
							+" \n Dislikes: "+modelo.darArreglo().firstElement().darDislikes());
					view.printMessage("-------");
					modelo.cargarId();
					view.printCategorias(modelo);
				} catch (IOException e) {
					
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}											
					break;

				case 2:
					view.printMessage("--------- \nSeleccione el requerimiento: ");
					dato = lector.next();
					if(dato.equals("1")){
						view.printMessage("Ingrese un país, numero y categoria(Str,int,str):");
						dato = lector.next();
						String[] i = dato.split(",");
						view.printPrimerosYUltimos(modelo.req1(i[0],Integer.parseInt(i[1]),i[2]));
					}
				break;

				case 3:
					view.printMessage("--------- \nDar tamaño de la nueva sublista:");
					dato = lector.next();
					ILista nuevo = modelo.subLista(Integer.parseInt(dato));
					view.printMessage("Copia exitosa. \nTamaño de la nueva lista: "+nuevo.size()+"\n---------");
					break;

				case 4:
					break;

				case 5: 
					view.printMessage("--------- \n Hasta pronto !! \n---------"); 
					lector.close();
					fin = true;
					break;				
				
				default: 
					view.printMessage("--------- \n Opcion Invalida !! \n---------");
					break;
			}
		}
		}
	}
