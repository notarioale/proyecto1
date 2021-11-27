
package Objetos;

import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * Clase del Objeto MensajeCliente, al que accedera el cliente para enviar y 
 * para recibir en formato JSON.
*/
public class MensajeClienteTCP {
    
    /**
     * String a Enviar desde el cliente al Server
    */
    public String createJSONString(String id_cliente, RegistroVehiculo registro, Integer opcion) {
        JSONObject objetoAux =  new JSONObject();
        
        objetoAux.put("id_cliente", id_cliente);
        objetoAux.put("opcion", opcion);
        objetoAux.put("cedula", registro.cedula);
        objetoAux.put("nombre", registro.nombre);
        objetoAux.put("apellido", registro.apellido);
        objetoAux.put("chapa", registro.chapa);
        objetoAux.put("marca", registro.marca);
        return objetoAux.toJSONString();
    }
    
    /**
     * String a enviar para solicitar desde el datos de una cedula especificada
     * al server.
    */
    public String createJSONString(String id_cliente, Integer opcion, String mensaje) {
        JSONObject objetoAux = new JSONObject();
        objetoAux.put("id_cliente", id_cliente);
        objetoAux.put("opcion", opcion);
        objetoAux.put("contenido", mensaje);
        
        return objetoAux.toJSONString();
    } 
    
    /**
     * Mensajes que recibe del server el cliente.
     */
    public JSONObject mensajeRecibido(String mensaje) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            //System.out.println("Recibido en clase mensaje: " + mensaje);
            obj = parser.parse(mensaje.trim());
        } catch (ParseException e) {
            System.out.println("Error en el parseo...");
        }
        JSONObject objetoAux = (JSONObject) obj;
        return objetoAux;
    }
}
