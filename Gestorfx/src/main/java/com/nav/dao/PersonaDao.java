package com.nav.dao;

import com.nav.entidades.Personal;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PersonaDao {

    private static final String ARCHIVO = "data/personal.csv";
    private static final List<Personal> listaPersonal = new ArrayList<>();

    static {
        cargarDesdeArchivo();
    }

    public void agregar(Personal p) {
        listaPersonal.add(p);
        guardarEnArchivo();
    }

    public List<Personal> listar() {
        return listaPersonal;
    }

    public Personal buscarPorDni(String dni) {
        for (Personal p : listaPersonal) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        return null;
    }

    public boolean eliminar(String dni) {
        Personal p = buscarPorDni(dni);
        if (p != null) {
            listaPersonal.remove(p);
            guardarEnArchivo();
            return true;
        }
        return false;
    }

    public boolean actualizar(Personal nuevoDatos) {
        Personal p = buscarPorDni(nuevoDatos.getDni());
        if (p != null) {
            p.setNombre(nuevoDatos.getNombre());
            p.setSueldo(nuevoDatos.getSueldo());
            guardarEnArchivo();
            return true;
        }
        return false;
    }

    private static void cargarDesdeArchivo() {
        Path ruta = Paths.get(ARCHIVO);
        if (!Files.exists(ruta)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
            listaPersonal.clear();
            String linea;
            while ((linea = reader.readLine()) != null) {
                Personal p = parsearLinea(linea);
                if (p != null) {
                    listaPersonal.add(p);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void guardarEnArchivo() {
        Path ruta = Paths.get(ARCHIVO);
        try {
            if (ruta.getParent() != null) {
                Files.createDirectories(ruta.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
                for (Personal p : listaPersonal) {
                    writer.write(formatearLinea(p));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Personal parsearLinea(String linea) {
        String[] partes = linea.split(",", -1);
        if (partes.length < 3) {
            return null;
        }
        return new Personal(partes[0], partes[1], partes[2]);
    }

    private static String formatearLinea(Personal p) {
        return String.join(",",
                limpiarCampo(p.getDni()),
                limpiarCampo(p.getNombre()),
                limpiarCampo(p.getSueldo()));
    }

    private static String limpiarCampo(String valor) {
        return valor == null ? "" : valor.replace(",", " ");
    }
}




    

