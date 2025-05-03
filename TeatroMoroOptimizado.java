
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class TeatroMoroOptimizado {

	static Scanner scanner = new Scanner(System.in);

	static String[] nombreAsientos = { "VIP", "Platea Alta", "Platea Baja", "Palco", "General" };
	static int[] precioAsientos = { 30000, 18000, 15000, 13000, 20000 };
	static int[] stockAsientos = { 5, 5, 5, 5, 5 };

	// Programa optimizado utilizando LinkedList para poder agregar y eliminar
	// elementos eficientemente.
	static LinkedList<Object> ventas = new LinkedList<Object>();
	static LinkedList<Object> reservas = new LinkedList<Object>();

	static double[] descuentos = { 1, 0.85, 0.90 };

	static int idReserva = 0;
	static int idCliente = 0;
	static int idVenta = 0;

	public static void main(String[] args) {
		try {
			int respuestaMenu;

			System.out.println("Bienvenido al Teatro Moro.\n");

			do {

				mostrarMenu();
				respuestaMenu = scanner.nextInt();

				switch (respuestaMenu) {
					case 1:
						System.out.println("Usted elijio hacer una reserva\n");
						Object[] datosCliente = identificarCliente();
						if ((boolean) datosCliente[3] == false) {
							System.out.println(
									"La verificacion de los datos cliente fallo.");
							return;
						}

						reservarEntrada(datosCliente);
						break;
					case 2:
						System.out.println("Usted elijio hacer una compra\n");
						comprarEntrada(reservas);
						break;
					case 3:
						System.out.println("Usted elijio modificar una compra\n");
						modificarVenta(ventas);
						break;
					case 4:
						System.out.println("Usted elijio eliminar una compra\n");
						eliminarVenta();
						break;
					case 5:
						System.out.println("Usted elijio ver las compras\n");
						mostrarVentas();
						break;
					case 6:
						System.out.println("Gracias por visitar Teatro Moro\n");
						break;

					default:
						System.out.println("Opcion invalida. Ingrese otra opcion.\n");
						break;
				}

			} while (respuestaMenu != 6);
			scanner.close();
		} catch (InputMismatchException e) {
			System.out.println("Error: seleccione una opcion valida");
		} finally {
			System.out.println("Fin del programa.");
			scanner.close();
		}
	}

	public static void mostrarMenu() {
		System.out.println("Por favor elija una de las siguientes opciones: ");
		System.out.println("1. Hacer una reserva");
		System.out.println("2. Hacer una compra");
		System.out.println("3. Modificar una compra");
		System.out.println("4. Eliminar una compra");
		System.out.println("5. Ver las compras");
		System.out.println("6. Salir");
		System.out.print("Seleccione una opcion: \n");

	}

	public static void mostrarAsientos() {

		for (int i = 0; i < nombreAsientos.length; i++) {
			System.out.println((i + 1) + ". Asiento " + nombreAsientos[i] + " - cantidad disponible: " + stockAsientos[i]);
		}

	}

	public static Object[] identificarCliente() {
		System.out.println("Ingrese su nombre: ");
		scanner.nextLine();
		String nombreCliente = scanner.nextLine();

		System.out.println("Ingrese su edad: ");
		int edadCliente = scanner.nextInt();

		boolean verificacion = true;
		if (nombreCliente.length() < 3 || !nombreCliente.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
			System.out.println("El nombre debe contener al menos 3 letras | Solo pueden ser letras y espacios.");
			verificacion = false;
		}

		if (edadCliente <= 0 || edadCliente >= 120) {
			System.out.println("La edad debe estar en un rango de 1 a 120");
			verificacion = false;
		}

		idCliente++;

		Object[] datosCliente = { idCliente, nombreCliente, edadCliente, verificacion };

		return datosCliente;

	}

	public static void reservarEntrada(Object[] datosCliente) {
		mostrarAsientos();

		System.out.println("Seleccione el asiento:");
		int indexReserva = scanner.nextInt() - 1;

		if (indexReserva > 4 || indexReserva < 0) {
			System.out.println("La opcion ingresada no es valida.");
			return;
		}

		if (stockAsientos[indexReserva] <= 0) {
			System.out.println("No hay stock para este asiento.");
			return;
		}

		System.out.println("Cuantas entradas desea reservar para el asiento " + nombreAsientos[indexReserva]);
		int cantidadEntradasReservadas = scanner.nextInt();

		if (cantidadEntradasReservadas > stockAsientos[indexReserva]) {
			System.out.println("No hay suficiente stock.");
			return;
		}

		if (cantidadEntradasReservadas <= 0) {
			System.out.println("Debe ingresar al menos 1 entrada.");
			return;
		}

		idReserva++;

		int indexAsiento = indexReserva;

		int idCliente = (int) datosCliente[0];
		String nombreCliente = (String) datosCliente[1];
		int edadCliente = (int) datosCliente[2];

		Object[] datosReserva = { idReserva, nombreAsientos[indexReserva], indexAsiento, cantidadEntradasReservadas,
				idCliente, nombreCliente, edadCliente };

		stockAsientos[indexReserva] -= cantidadEntradasReservadas;

		reservas.add(datosReserva);

		System.out.println("Su reserva fue ingresada exitosamente con los siguientes datos: ");
		System.out.println("----------");
		System.out.println("ID de reserva: " + idCliente);
		System.out.println("Nombre del asiento: " + datosReserva[1]);
		System.out
				.println("Cantidad de entradas para " + datosReserva[1] + ": " + datosReserva[3]);
		System.out.println("----------");

	}

	public static int calcularTotalBruto(int precio, int cantidadEntradas) {
		return precio * cantidadEntradas;
	}

	public static int calcularDescuento(int sumaTotal, int edadCliente) {

		double descuento;

		if (edadCliente < 18) {
			descuento = descuentos[2];
		} else if (edadCliente >= 65) {
			descuento = descuentos[1];
		} else {
			descuento = descuentos[0];
		}

		return (int) (sumaTotal * descuento);
	}

	public static void comprarEntrada(LinkedList<Object> reservas) {

		if (reservas.isEmpty()) {
			System.out.println("Usted no tiene ninguna reserva.\n");
			return;
		}

		for (Object reserva : reservas) {

			Object[] fila = (Object[]) reserva;

			int precioBruto = calcularTotalBruto(precioAsientos[(int) fila[2]], (int) fila[3]);
			int precioConDescuento = calcularDescuento(precioBruto, (int) fila[6]);
			idVenta++;

			String nombreAsiento = (String) fila[1];
			int cantidadEntradas = (int) fila[3];
			int idCliente = (int) fila[4];
			String nombreCliente = (String) fila[5];
			int indexAsiento = (int) fila[2];

			Object[] venta = { idVenta, nombreAsiento, cantidadEntradas, precioConDescuento, idCliente, nombreCliente,
					indexAsiento };

			ventas.add(venta);
		}

		System.out.println("Compra realizada exitosamente.\n");

		reservas.clear();

	}

	public static void mostrarVentas() {

		if (ventas.size() == 0) {
			System.out.println("No hay ventas para mostrar\n");
		}

		for (int i = 0; i < ventas.size(); i++) {

			Object[] fila = (Object[]) ventas.get(i);
			System.out.println((i + 1) + ". ID compra " + (int) fila[0] + " | Ubicacion: " + (String) fila[1]
					+ " | cantidad: "
					+ (int) fila[2] + " | total: " + (int) fila[3] + " | ID cliente: " + (int) fila[4] + " | nombre cliente: "
					+ (String) fila[5] + "\n");
		}

	}

	public static void modificarVenta(LinkedList<Object> ventas) {
		if (ventas.isEmpty()) {
			System.out.println("Usted no tiene ninguna compra realizada.\n");
			return;
		}

		mostrarVentas();

		System.out.println("Seleccione la venta que desea modificar: ");
		int indexModificar = scanner.nextInt() - 1;

		System.out.println("Ingrese la nueva cantidad de entradas: ");
		int nuevaCantEntradas = scanner.nextInt();
		if (nuevaCantEntradas <= 0) {
			System.out.println("No puede elegir 0.");
			return;
		}

		Object[] venta = (Object[]) ventas.get(indexModificar);

		if ((int) venta[2] == nuevaCantEntradas) {
			System.out.println("La nueva cantidad no puede ser igual a la cantidad anterior.");
			return;
		}

		int indexAsiento = (int) venta[6];
		stockAsientos[indexAsiento] = stockAsientos[indexAsiento] + (int) venta[2];
		stockAsientos[indexAsiento] = stockAsientos[indexAsiento] - nuevaCantEntradas;

		int nuevoPrecio = ((int) venta[3] / (int) venta[2]) * nuevaCantEntradas;

		venta[3] = nuevoPrecio;
		venta[2] = nuevaCantEntradas;

		System.out.println("Venta modificada exitosamente.\n");

	}

	public static void eliminarVenta() {
		if (ventas.isEmpty()) {
			System.out.println("Usted no tiene ninguna compra realizada.\n");
			return;
		}

		mostrarVentas();

		System.out.println("Seleccione la venta que desea eliminar: ");
		int indexEliminar = scanner.nextInt() - 1;

		ventas.remove(indexEliminar);

	}

}
