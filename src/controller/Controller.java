package controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import model.data_structures.ILista;
import model.logic.Modelo;
import model.logic.YoutubeVideo;
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
						ILista<YoutubeVideo> r1 = modelo.req1(i[0].replace("-"," ").trim(),Integer.parseInt(i[1]),i[2].replace("-"," ").trim());
						if(r1!=null)
							view.imprimirVideoReq1(r1,r1.size());
					}
					else if(dato.equals("2")){
						view.printMessage("Ingrese un pais:");
						dato = lector.next();
						view.printMessage(modelo.req2(dato.replace("-"," ").trim()));
					}
					else if(dato.equals("3")){
						view.printMessage("Ingrese una categoria:");
						dato = lector.next();
						view.printMessage(modelo.req3(dato.replace("-"," ").trim()));
					}
					else if(dato.equals("4")){
						view.printMessage("Ingrese un país, numero y etiqueta(Str,int,str):");
						dato = lector.next();
						String[] i = dato.split(",");
						ILista<YoutubeVideo> r1 = modelo.req4(i[0].replace("-"," ").trim(),Integer.parseInt(i[1]),i[2].replace("-"," ").trim());
						if(r1!=null)
							view.imprimirVideoReq4(r1,r1.size());
					}
				break;

				case 3: 
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
