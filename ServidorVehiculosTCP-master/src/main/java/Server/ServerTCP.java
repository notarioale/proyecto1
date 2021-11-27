package Server;
import Objetos.MensajeServerTCP;
import Objetos.RegistroVehiculo;
import java.util.concurrent.TimeUnit;
import java.net.*;
import java.io.*;
import java.util.LinkedList;
import org.json.simple.JSONObject;

public class ServerTCP {
    
    static LinkedList<RegistroVehiculo> listaRegistroVehiculo = new LinkedList<RegistroVehiculo>();   
    
    public static void main(String[] args) throws Exception {

        int puertoServidor = 5000;
        int tiempo_procesamiento_miliseg = 2000;
		
		try{
			tiempo_procesamiento_miliseg = Integer.parseInt(args[0]);
		}catch(Exception e1){
			System.out.println("Se omite el argumento, tiempo de procesamiento " + tiempo_procesamiento_miliseg  + ". Ref: " + e1);
		}
		
		
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(puertoServidor);
        } catch (IOException e) {
            System.err.println("No se puede abrir el puerto: " +puertoServidor+ ".");
            System.exit(1);
        }
        System.out.println("Puerto abierto: "+puertoServidor+".");
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Fallo el accept().");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                clientSocket.getInputStream()));

        
        // Creamos unos cuantos registros para las pruebas
        listaRegistroVehiculo.add(new RegistroVehiculo("5767078", "Alejandro", "Notario", "ABC123", "Peugeot"));
        listaRegistroVehiculo.add(new RegistroVehiculo("111222", "Enrique", "Iglesias", "BBB123", "VW"));
        listaRegistroVehiculo.add(new RegistroVehiculo("111222", "Enrique", "Iglesias", "ABC222", "VW"));
        listaRegistroVehiculo.add(new RegistroVehiculo("777888", "Maria", "Sanchez", "ZZZ111", "Toyota"));
        listaRegistroVehiculo.add(new RegistroVehiculo("5767078", "Alejandro", "Notario", "AMV134", "Ford"));
        listaRegistroVehiculo.add(new RegistroVehiculo("910123", "Mabel", "Espinola", "WQS298", "Mercedes"));
        listaRegistroVehiculo.add(new RegistroVehiculo("123456", "Alejandro", "Notario", "WXY789", "Citröen"));
        out.println("Bienvenido!");
        String inputLine, outputLine;
        Boolean continuar = true;
        MensajeServerTCP mensaje = new MensajeServerTCP();
        JSONObject objetoAux;
        while(continuar) {
            inputLine = in.readLine();
            objetoAux=mensaje.mensajeRecibido(inputLine);
            //Long opcion = (Long) objetoAux.get("opcion");
            Integer opcion =  ((Long) objetoAux.get("opcion")).intValue();

            // Swicth case para las opciones.
            switch(opcion) {
                case 1:
                    // Agregar nuevo registro
                    String id_cliente = (String) objetoAux.get("id_cliente");
                    System.out.println("El cliente " + id_cliente + " agrega un registro");
                    String cedula = (String) objetoAux.get("cedula");
                    String nombre = (String) objetoAux.get("nombre");
                    String apellido = (String) objetoAux.get("apellido");
                    String chapa = (String) objetoAux.get("chapa");
                    String marca = (String) objetoAux.get("marca");
                    
                    RegistroVehiculo registro = new RegistroVehiculo(cedula, nombre, apellido, chapa, marca);
                    listaRegistroVehiculo.add(registro);
                    JSONObject aux = new JSONObject();
                    aux.put("contenido", "Registro agregado con exito!");
                    outputLine = aux.toJSONString();
                    System.out.println("Enviamos al cliente: " + outputLine);
                    out.println(outputLine);
                    break;
                case 2:
                    // Buscar y retornar lista de registros con la cedula recibida
                    String cedulaBuscar = (String) objetoAux.get("contenido");
                    JSONObject auxEnviar = new JSONObject();
                    auxEnviar.put("id_cliente", (String) objetoAux.get("id_cliente"));
                    Integer cantidadRegistros = 0;
                    for(RegistroVehiculo reg : listaRegistroVehiculo) {
                        if (reg.getCedula().equals(cedulaBuscar)) {
                            cantidadRegistros++;
                            String aEnviar = "\tCedula: " + reg.getCedula() + "\n"
                                    + "\tNombre: " + reg.getNombre() + "\n"
                                    + "\tApellido: " + reg.getApellido() + "\n"
                                    + "\tChapa: " + reg.getChapa() + "\n"
                                    + "\tMarca: " + reg.getMarca();
                            String aux2 = "registro" + Integer.toString(cantidadRegistros);
                            System.out.println(aux2 + ":" + aEnviar);
                            auxEnviar.put(aux2, aEnviar);
                        }
                    }
                    // Cantidad de registros a enviar
                    auxEnviar.put("cantidad_registros", cantidadRegistros );
                    // Envia al cliente la informacion requerida
                    out.println(auxEnviar.toJSONString());
                    break;
                case 3:
                    // Terminar la ejecucion del server
                    System.out.println("Finalizando ejecucion del server...");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opcion incorrecta!!!");
            }
        }
        //inputLine = in.readLine();
        //System.out.println("Mensaje recibido: " + inputLine);
        //outputLine = "Respuesta igual al recibido: " + inputLine;

	//	TimeUnit.MILLISECONDS.sleep(tiempo_procesamiento_miliseg);
		
        //out.println(outputLine);

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

}
