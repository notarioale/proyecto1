
package Cliente;

import Objetos.MensajeClienteTCP;
import Objetos.RegistroVehiculo;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.simple.JSONObject;

public class ClienteTCP {

 public static void main(String[] args) throws IOException {

        Socket unSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        Scanner lectura = new Scanner(System.in);
        System.out.println("Ingrese el id Cliente: ");
        String id_cliente = lectura.nextLine();
        try {
            unSocket = new Socket("localhost", 5000);
            // enviamos nosotros
            out = new PrintWriter(unSocket.getOutputStream(), true);

            //viene del servidor
            in = new BufferedReader(new InputStreamReader(unSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host desconocido");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de I/O en la conexion al host");
            System.exit(1);
        }

        
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        MensajeClienteTCP mensaje = new MensajeClienteTCP();

        String fromServer;
        String fromUser;
        Boolean continuar = true;
        JSONObject objetoAux;
        fromServer = in.readLine();
        System.out.println(fromServer);
        while (continuar) {
            /*System.out.println("Servidor: " + fromServer);
            if (fromServer.equals("Bye")) {
                break;
            }*/
            menu();
            System.out.println("Opcion: ");
            Integer opcion = lectura.nextInt();
            lectura.nextLine();
            switch(opcion) {
                case 1:
                    // Creamos un nuevo registro para enviar al server.
                    System.out.println("Ingrese los siguientes datos de registro: ");
                    System.out.println("Cedula: ");
                    String cedula;
                    cedula = lectura.nextLine();
                    //System.out.println("---> " + cedula);
                    System.out.println("Nombre: ");
                    String nombre = lectura.nextLine();
                    //System.out.println("---> " + nombre);
                    System.out.println("Apellido: ");
                    String apellido = lectura.nextLine();
                    //System.out.println("---> " + apellido);
                    System.out.println("Chapa: ");
                    String chapa = lectura.nextLine();
                    //System.out.println("---> " + chapa);
                    System.out.println("Marca: ");
                    String marca = lectura.nextLine();
                    //System.out.println("---> " + marca);
                    RegistroVehiculo registroNuevo = new RegistroVehiculo(cedula, nombre, apellido, chapa, marca);
                    fromUser = mensaje.createJSONString(id_cliente, registroNuevo, opcion);
                    out.println(fromUser);
                    fromUser="";
                    // Esperamos la respuesta del server
                    // Si es null, significa que ocurrio un error y cancelamos la ejecucion.
                    fromServer = in.readLine();
                    if (fromServer == null) {
                        System.out.println("Mensaje no recibido...");
                        System.out.println("Fin de la ejecucion...");
                        System.exit(0);
                    }
                    System.out.println("Mensaje de Confirmacion: ");
                    objetoAux = mensaje.mensajeRecibido(fromServer);
                    System.out.println("Mensaje: " + (String) objetoAux.get("contenido"));
                    fromServer="";
                    System.out.println("\nPress enter to continue...");
                    lectura.nextLine();
                    break;
                case 2:
                    // Consultamos la lista de vehiculos de una cedula especificada
                    System.out.println("Ingrese la cedula a buscar: ");
                    String cedulaBuscar = lectura.nextLine();
                    fromUser = mensaje.createJSONString(id_cliente, opcion, cedulaBuscar);
                    out.println(fromUser);
                    fromUser="";
                    // Esperamos la respuesta del server
                    // Si es null, significa que ocurrio un error y cancelamos la ejecucion.
                    fromServer = in.readLine();
                    if (fromServer == null) {
                        System.out.println("Mensaje no recibido...");
                        System.out.println("Fin de la ejecucion...");
                        System.exit(0);
                    }
                    objetoAux = mensaje.mensajeRecibido(fromServer);
                    // El formato del objeto es: "id_cliente:, cantidad_registros: , registro1: , registro2: ..."
                    if (((Long) objetoAux.get("cantidad_registros")).intValue() == 0 ) {
                        System.out.println("No hay registros de CI: " + cedulaBuscar);
                    } else {
                        Integer cantidad_registros = ((Long) objetoAux.get("cantidad_registros")).intValue();
                        for(int i = 0; i < cantidad_registros; i++) {
                            String extraerRegistro = "registro" + String.valueOf(i+1);
                            System.out.println("Registro " + (i+1) + ": " + (String) objetoAux.get(extraerRegistro));
                        }
                       System.out.println("Fin de lista...");
                    }
                    fromServer="";
                    break;
                case 3:
                    System.out.println("Finalizando cliente...");
                    fromUser = mensaje.createJSONString(id_cliente, opcion, "Cliente finalizado");
                    out.println(fromUser);
                    fromUser="";
                    continuar=false;
                    break;
                default:
                    System.out.println("Operacion No Soportada!!");
            }
//            fromUser = stdIn.readLine();
//            if (fromUser != null) {
//                System.out.println("Cliente: " + fromUser);

                //escribimos al servidor
                //out.println(fromUser);
        }
        
        out.close();
        in.close();
        stdIn.close();
        unSocket.close();
    }
 
    public static void menu() {
        System.out.println("\tOperaciones Permitidas");
        System.out.println(" 1 - Agregar nuevo registro");
        System.out.println(" 2 - Consultar por un registro");
        System.out.println(" 3 - Salir...");
    }
}
