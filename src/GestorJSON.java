import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestorJSON {
    private static final String RUTA_DEFAULT = "melodia.json";
    private final Gson gson;

    @SuppressWarnings("unused")
    public GestorJSON() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public boolean guardar(ListaLigada lista, String ruta) {
        if (lista == null || lista.isEmpty()) {
            return false;
        }

        try {
            List<NotaDTO> notasDTO = new ArrayList<>();

            for (int i = 0; i < lista.getTamanho(); i++) {
                Nota nota = lista.obtener(i);
                if (nota != null) {
                    NotaDTO dto = new NotaDTO(
                            nota.getNombreNota(),
                            nota.getFigura().name(),
                            nota.getOctava());
                    notasDTO.add(dto);
                }
            }

            if (notasDTO.isEmpty()) {
                return false;
            }

            String json = gson.toJson(notasDTO);

            try (FileWriter writer = new FileWriter(ruta)) {
                writer.write(json);
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
            return false;
        }
    }

    public boolean guardar(ListaLigada lista) {
        return guardar(lista, RUTA_DEFAULT);
    }

    public ListaLigada cargar(String ruta) {
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            System.err.println("El archivo no existe: " + ruta);
            return new ListaLigada();
        }

        if (!archivo.canRead()) {
            System.err.println("No se puede leer el archivo: " + ruta);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea);
            }

            String jsonContenido = contenido.toString().trim();
            if (jsonContenido.isEmpty()) {
                return new ListaLigada();
            }

            Type listaTipo = new TypeToken<List<NotaDTO>>() {
            }.getType();
            @SuppressWarnings("unchecked")
            List<NotaDTO> notasDTO = (List<NotaDTO>) gson.fromJson(jsonContenido, listaTipo);

            if (notasDTO == null || notasDTO.isEmpty()) {
                return new ListaLigada();
            }

            ListaLigada listaCargada = new ListaLigada();
            for (NotaDTO dto : notasDTO) {
                try {
                    Figura figura = Figura.valueOf(dto.getFigura());
                    Nota nota = new Nota(dto.getNombreNota(), figura, dto.getOctava());
                    listaCargada.agregar(nota);
                } catch (Exception e) {
                    System.err.println("Error al procesar nota: " + e.getMessage());
                }
            }

            return listaCargada;
        } catch (IOException e) {
            System.err.println("Error al cargar: " + e.getMessage());
            return null;
        }
    }

    public ListaLigada cargar() {
        return cargar(RUTA_DEFAULT);
    }

    private static class NotaDTO {
        private String nombreNota;
        private String figura;
        private int octava;

        public NotaDTO() {
        }

        public NotaDTO(String nombreNota, String figura, int octava) {
            this.nombreNota = nombreNota;
            this.figura = figura;
            this.octava = octava;
        }

        public String getNombreNota() {
            return nombreNota;
        }

        public void setNombreNota(String nombreNota) {
            this.nombreNota = nombreNota;
        }

        public String getFigura() {
            return figura;
        }

        public void setFigura(String figura) {
            this.figura = figura;
        }

        public int getOctava() {
            return octava;
        }

        public void setOctava(int octava) {
            this.octava = octava;
        }
    }
}