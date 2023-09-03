package unison;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Vendor {
    public static final int MAX_NAME = 35;
    public static final int MAX_ZONE = 15;
    public static final int MAX_VENTA = 10;

    public static final int RECORD_LEN = 72;

    private int codigo;         //4bytes         // 4 bytes
    private String nombre;      //100 bytes          // 35 bytes
    private Date fecha;         //10bytes          // 8 bytes, almacenado en long
    private String zona;        //50 bytes           // 15 bytes
    private String ventas;      // 10 bytes
    public String getVentas() {
        return ventas;
    }

    public void setVentas(String ventas) {
        int len = ventas.length();
        if( len < MAX_VENTA ) {
            StringBuilder sb = new StringBuilder(ventas);
            int count = MAX_VENTA - len;
            for(int i = 0;  i < count; i++) {
                sb.append(' ');
            }
            this.ventas = sb.toString();
        } else {
            this.ventas = ventas;
        }
    }



    public Vendor(int codigo, String nombre, Date fecha, String zona) {
        super();

        this.setCodigo(codigo);
        this.setNombre(nombre);
        this.setFecha(fecha);
        this.setZona(zona)  ;

    }
    public Vendor(int codigo, String nombre, Date fecha, String zona, String venta) {
        super();

        this.setCodigo(codigo);
        this.setNombre(nombre);
        this.setFecha(fecha);
        this.setZona(zona)  ;
        this.setVentas(venta);
    }

    public Vendor(int codigo, String nombre, String fecha, String zona, String venta) {
        super();
        this.setCodigo(codigo);
        this.setNombre(nombre);
        this.setFecha(fecha);
        this.setZona(zona)  ;
        this.setVentas(venta);
    }

    public Vendor() {
        super();
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getZona() {
        return zona;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        int len = nombre.length();
        if( len < MAX_NAME ) {
            StringBuilder sb = new StringBuilder(nombre);
            int count = MAX_NAME - len;
            // rellenar con espacios en blanco a la derecha
            for(int i = 0;  i<count; i++) {
                sb.append(' ');
            }
            this.nombre = sb.toString();
        } else {
            this.nombre = nombre;
        }
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setFecha(String fecha) {
        //StringTokenizer parser = new StringTokenizer(fecha,"/");

        if (fecha.matches("\\d{2}/\\d{2}/\\d{4}")){
            Scanner parser = new Scanner( new StringReader(fecha) );
            parser.useDelimiter("/");

            int month = parser.nextInt();
            int day = parser.nextInt();
            int year = parser.nextInt();

            GregorianCalendar calendar = new GregorianCalendar(year,month, day);

            this.fecha = calendar.getTime();
        }else {
            System.err.println("Formato no permitido: " + fecha);
            this.fecha = null;
        }

    }

    public void setZona(String zona) {
        int len = zona.length();
        if( len < MAX_ZONE ) {
            StringBuilder sb = new StringBuilder(zona);
            int count = MAX_ZONE - len;
            for(int i = 0;  i < count; i++) {
                sb.append(' ');
            }
            this.zona = sb.toString();
        } else {
            this.zona = zona;
        }
    }

    public String fechaString() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        return f.format(fecha);
    }
    @Override
    public String toString() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        // regresa una cadena en formato CSV
        return codigo + "," + nombre + "," + f.format(fecha) + "," + zona + "," + ventas+",";
    }
}
