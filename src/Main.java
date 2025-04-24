import java.util.Scanner;
import java.util.regex.Pattern;

class ValidadorContrasena extends Thread {
    private String contrasena;

    public ValidadorContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void run() {
        System.out.println("Validando contraseña: " + contrasena);

        boolean valida = true;

        if (contrasena.length() < 8) {
            System.out.println("Debe tener al menos 8 caracteres.");
            valida = false;
        }

        if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(contrasena).find()) {
            System.out.println("Debe contener al menos 1 carácter especial.");
            valida = false;
        }

        int mayusculas = contarCoincidencias(contrasena, "[A-Z]");
        if (mayusculas < 2) {
            System.out.println("Debe contener al menos 2 letras mayúsculas.");
            valida = false;
        }

        int minusculas = contarCoincidencias(contrasena, "[a-z]");
        if (minusculas < 3) {
            System.out.println("Debe contener al menos 3 letras minúsculas.");
            valida = false;
        }

        if (!Pattern.compile("[0-9]").matcher(contrasena).find()) {
            System.out.println("Debe contener al menos 1 número.");
            valida = false;
        }

        if (valida) {
            System.out.println("Contraseña válida.");
        } else {
            System.out.println("Contraseña inválida.");
        }

        System.out.println("----------------------------");
    }

    private int contarCoincidencias(String texto, String regex) {
        int contador = 0;
        var matcher = Pattern.compile(regex).matcher(texto);
        while (matcher.find()) {
            contador++;
        }
        return contador;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("        REQUISITOS PARA VALIDAR UNA CONTRASEÑA");
        System.out.println("- Mínimo 8 caracteres");
        System.out.println("- Al menos 1 carácter especial (!@#$%^&*(),.?\":{}|<>)");
        System.out.println("- Al menos 2 letras mayúsculas");
        System.out.println("- Al menos 3 letras minúsculas");
        System.out.println("- Al menos 1 número");
        System.out.println();

        System.out.print("¿Cuántas contraseñas quieres validar?: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        ValidadorContrasena[] hilos = new ValidadorContrasena[cantidad];

        for (int i = 0; i < cantidad; i++) {
            System.out.print("Ingresa la contraseña #" + (i + 1) + ": ");
            String contrasena = scanner.nextLine();
            hilos[i] = new ValidadorContrasena(contrasena);
        }

        System.out.println("\nIniciando validaciones concurrentes...\n");

        for (ValidadorContrasena hilo : hilos) {
            hilo.start();
        }

        for (ValidadorContrasena hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Validaciones finalizadas.");
    }
}
