package unison;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final String FILE_PATH = "vendors.dat"; // Ruta al archivo dat
    private static List<Vendor> vendors; // Lista para almacenar los vendedores

    public static void main(String[] args) {
        int option;
        try (Scanner scanner = new Scanner(System.in)) {
            vendors = loadVendorsFromFile(); // Cargar los datos del archivo al inicio del programa

            do {
                for(Vendor v: vendors){
                    System.out.println(v);
                }
                System.out.println("1. Agregar nuevo vendedor");
                System.out.println("2. Borrar vendedor");
                System.out.println("3. Modificar vendedor");
                System.out.println("4. Consultar por zona");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        addVendor(scanner);
                        break;
                    case 2:
                        deleteVendor(scanner);
                        break;
                    case 3:
                        modifyVendor(scanner);
                        break;
                    case 4:
                        scanner.nextLine(); // Consume el salto de línea anterior
                        System.out.print("Ingrese la zona para la consulta: ");
                        String zonaConsulta = scanner.nextLine();
                        queryByZone(zonaConsulta);
                        break;
                    case 5:
                        //saveVendorsToFile(vendors); // Guardar al salir
                        System.out.println("Cambios guardados en el archivo.");
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                }
            } while (option != 5);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }


    private static List<Vendor> loadVendorsFromFile() {
        List<Vendor> vendors = new ArrayList<>();

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(FILE_PATH, "r")) {
            long fileLength = randomAccessFile.length();
            long currentPosition = 0;

            while (currentPosition < fileLength) {
                // Verificar si hay suficientes bytes para leer un entero
                if (fileLength - currentPosition < 4) {
                    // Si no hay suficientes bytes, salir del bucle
                    break;
                }

                int id = randomAccessFile.readInt();
                String name = readString(randomAccessFile, 100);
                String fechaStr = readString(randomAccessFile, 10);
                String zone = readString(randomAccessFile, 50);
                String venta = readString(randomAccessFile, 10);

                Date fecha;
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    fecha = dateFormat.parse(fechaStr);
                } catch (ParseException e) {
                    // Manejar la excepción de fecha incorrecta
                    System.err.println("Advertencia: Fecha incorrecta en el registro, se omitirá.");
                    continue; // Continuar con el próximo registro
                }

                vendors.add(new Vendor(id, name, fecha, zone, venta));
                currentPosition = randomAccessFile.getFilePointer();
            }
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vendors;
    }



    private static void addVendor(Scanner scanner) throws IOException, ParseException {
        System.out.print("Ingrese el código del nuevo vendedor: ");
        int codigo = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Ingrese el nombre del nuevo vendedor: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese la fecha de nacimiento del nuevo vendedor (dd/mm/yyyy): ");
        String fechaStr = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = dateFormat.parse(fechaStr);

        System.out.print("Ingrese la zona del nuevo vendedor: ");
        String zona = scanner.nextLine();

        System.out.print("Ingrese las ventas del nuevo vendedor: ");
        String venta = scanner.nextLine();

        Vendor nuevoVendedor = new Vendor(codigo, nombre, fecha, zona, venta);
        vendors.add(nuevoVendedor);


        saveVendorsToFile(vendors);
        System.out.println("Nuevo vendedor agregado con éxito.");
        System.out.println("Cambios guardados en el archivo.");
    }

    private static void deleteVendor(Scanner scanner) {
        try {
            System.out.print("Ingrese el código del vendedor a borrar: ");
            int codigoABorrar = scanner.nextInt();

            boolean borrado = false;

            Iterator<Vendor> iterator = vendors.iterator();
            while (iterator.hasNext()) {
                Vendor vendedor = iterator.next();
                if (vendedor.getCodigo() == codigoABorrar) {
                    iterator.remove();
                    borrado = true;
                    System.out.println("Vendedor borrado con éxito.");
                    break;
                }
            }

            if (!borrado) {
                System.out.println("No se encontró un vendedor con ese código.");
            } else {
                // Actualizar el archivo .dat con la lista actualizada
                saveVendorsToFile(vendors);
                System.out.println("Cambios guardados en el archivo.");
            }
        } catch (InputMismatchException | IOException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
        }
    }


    private static void modifyVendor(Scanner scanner) {
        System.out.print("Ingrese el código del vendedor a modificar: ");
        int codigoAModificar = scanner.nextInt();
        boolean modificado = false;

        for (Vendor vendedor : vendors) {
            if (vendedor.getCodigo() == codigoAModificar) {
                System.out.println("Seleccionado vendedor: " + vendedor.getNombre());
                System.out.println("1. Modificar nombre");
                System.out.println("2. Modificar fecha de nacimiento");
                System.out.println("3. Modificar zona");
                System.out.println("4. Modificar ventas");
                System.out.println("5. Cancelar");

                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        System.out.print("Nuevo nombre: ");
                        String nuevoNombre = scanner.nextLine();
                        vendedor.setNombre(nuevoNombre);
                        System.out.println("Nombre modificado con éxito.");
                        modificado = true;
                        break;
                    case 2:
                        System.out.print("Nueva fecha de nacimiento (dd/mm/yyyy): ");
                        String nuevaFecha = scanner.nextLine();
                        vendedor.setFecha(nuevaFecha);
                        System.out.println("Fecha de nacimiento modificada con éxito.");
                        modificado = true;
                        break;
                    case 3:
                        System.out.print("Nueva zona: ");
                        String nuevaZona = scanner.nextLine();
                        vendedor.setZona(nuevaZona);
                        System.out.println("Zona modificada con éxito.");
                        modificado = true;
                        break;
                    case 4:
                        System.out.print("Nueva total de venta: ");
                        String nuevaVenta = scanner.nextLine();
                        vendedor.setVentas(nuevaVenta);
                        System.out.println("Venta modificada con éxito.");
                        modificado = true;
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                }

                // Guardar los cambios en el archivo
                if (modificado) {
                    try {
                        saveVendorsToFile(vendors);
                        System.out.println("Cambios guardados en el archivo.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            }
        }

        if (!modificado) {
            System.out.println("No se encontró un vendedor con ese código.");
        }
    }


    private static void queryByZone(String zonaConsulta) {
        List<Vendor> vendedoresEnZona = vendors.stream()
                .filter(vendedor -> vendedor.getZona().trim().equals(zonaConsulta.trim()))
                .collect(Collectors.toList());

        if (!vendedoresEnZona.isEmpty()) {
            System.out.println("Vendedores en la zona '" + zonaConsulta + "':");
            for (Vendor vendedor : vendedoresEnZona) {
                System.out.println("Código: " + vendedor.getCodigo());
                System.out.println("Nombre: " + vendedor.getNombre());
                System.out.println("Fecha de Nacimiento: " + vendedor.getFecha());
                System.out.println("Zona: " + vendedor.getZona());
                System.out.println("Venta: " + vendedor.getVentas());
                System.out.println();
            }
        } else {
            System.out.println("No se encontraron vendedores en la zona '" + zonaConsulta + "'.");
        }
    }

    private static void saveVendorsToFile(List<Vendor> vendors) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(FILE_PATH, "rws")) {
            // Ubicamos el puntero al final del archivo para agregar los datos
            randomAccessFile.setLength(0);

            for (Vendor newVendor : vendors) {
                randomAccessFile.writeInt(newVendor.getCodigo());
                writeString(randomAccessFile, newVendor.getNombre(), 100);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(newVendor.getFecha());
                writeString(randomAccessFile, formattedDate, 10);
                writeString(randomAccessFile, newVendor.getZona(), 50);
                writeString(randomAccessFile, newVendor.getVentas(), 10);
            }
            randomAccessFile.close();
        }
    }

    private static String readString(RandomAccessFile randomAccessFile, int length) throws IOException {
        byte[] buffer = new byte[length];
        randomAccessFile.readFully(buffer);
        return new String(buffer, "UTF-8").trim();
    }
    private static void writeString(RandomAccessFile randomAccessFile, String value, int length) throws IOException {
        byte[] buffer = new byte[length];
        Arrays.fill(buffer, (byte) ' '); // Rellenar con espacios en blanco
        byte[] valueBytes = value.getBytes("UTF-8");
        System.arraycopy(valueBytes, 0, buffer, 0, Math.min(valueBytes.length, length));
        randomAccessFile.write(buffer);
    }
}
