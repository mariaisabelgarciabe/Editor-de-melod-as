import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;


public class InterfazEditor extends JFrame {
    private ListaLigada lista;
    private GestorJSON gestorJSON;
    private ReproductorMidi reproductor;

    private JTextField campoNota;
    private JComboBox<Figura> comboFigura;
    private JSpinner spinnerOctava;
    private JList<String> listaNotas;
    private DefaultListModel<String> modeloLista;

    private int indiceSeleccionado = -1;
    private static final Set<String> NOTAS_VALIDAS;

    static {
        NOTAS_VALIDAS = new HashSet<>();
        NOTAS_VALIDAS.add("DO");
        NOTAS_VALIDAS.add("C");
        NOTAS_VALIDAS.add("DO#");
        NOTAS_VALIDAS.add("C#");
        NOTAS_VALIDAS.add("RE");
        NOTAS_VALIDAS.add("D");
        NOTAS_VALIDAS.add("RE#");
        NOTAS_VALIDAS.add("D#");
        NOTAS_VALIDAS.add("MI");
        NOTAS_VALIDAS.add("E");
        NOTAS_VALIDAS.add("FA");
        NOTAS_VALIDAS.add("F");
        NOTAS_VALIDAS.add("FA#");
        NOTAS_VALIDAS.add("F#");
        NOTAS_VALIDAS.add("SOL");
        NOTAS_VALIDAS.add("G");
        NOTAS_VALIDAS.add("SOL#");
        NOTAS_VALIDAS.add("G#");
        NOTAS_VALIDAS.add("LA");
        NOTAS_VALIDAS.add("A");
        NOTAS_VALIDAS.add("LA#");
        NOTAS_VALIDAS.add("A#");
        NOTAS_VALIDAS.add("SI");
        NOTAS_VALIDAS.add("B");
        NOTAS_VALIDAS.add("REB");
        NOTAS_VALIDAS.add("MIB");
        NOTAS_VALIDAS.add("SOLB");
        NOTAS_VALIDAS.add("LAB");
        NOTAS_VALIDAS.add("SIB");
    }

    public InterfazEditor() {
        lista = new ListaLigada();
        gestorJSON = new GestorJSON();
        reproductor = new ReproductorMidi();

        inicializarComponentes();
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        setTitle("Editor de Melodías 🎵");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        agregarPanelEntrada();
        agregarPanelLista();
        agregarPanelBotones();
    }

    private void agregarPanelEntrada() {
        JPanel panelSuperior = new JPanel(new GridLayout(4, 2, 12, 12));
        panelSuperior.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel etiquetaNota = crearEtiqueta("Nota musical:", "Ejemplo: DO, RE, MI, FA, SOL, LA, SI");
        campoNota = new JTextField();
        
        JLabel etiquetaFigura = crearEtiqueta("Duración:", "Seleccione la figura musical");
        comboFigura = new JComboBox<>(Figura.values());
        
        JLabel etiquetaOctava = crearEtiqueta("Octava:", "Rango: 1-8");
        spinnerOctava = new JSpinner(new SpinnerNumberModel(4, 1, 8, 1));

        panelSuperior.add(etiquetaNota);
        panelSuperior.add(campoNota);
        panelSuperior.add(etiquetaFigura);
        panelSuperior.add(comboFigura);
        panelSuperior.add(etiquetaOctava);
        panelSuperior.add(spinnerOctava);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private JLabel crearEtiqueta(String texto, String tooltip) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setToolTipText(tooltip);
        return etiqueta;
    }

    private void agregarPanelLista() {
        modeloLista = new DefaultListModel<>();
        listaNotas = new JList<>(modeloLista);
        listaNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaNotas.addListSelectionListener(this::manejarSeleccionLista);

        JScrollPane scrollPane = new JScrollPane(listaNotas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Notas en la melodía 🎹"));
        scrollPane.setPreferredSize(new Dimension(0, 200));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void manejarSeleccionLista(ListSelectionEvent evento) {
        if (!evento.getValueIsAdjusting()) {
            indiceSeleccionado = listaNotas.getSelectedIndex();
            if (indiceSeleccionado >= 0) {
                cargarNotaEnCampos(indiceSeleccionado);
            }
        }
    }

    private void agregarPanelBotones() {
        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10));
        panelBotones.setBorder(new EmptyBorder(10, 15, 15, 15));

        JButton botonAgregar = crearBoton("➕ Agregar", "Agregar una nueva nota", e -> agregarNota());
        JButton botonModificar = crearBoton("✏️ Modificar", "Modificar la nota seleccionada", e -> modificarNota());
        JButton botonEliminar = crearBoton("❌ Eliminar", "Eliminar la nota seleccionada", e -> eliminarNota());
        JButton botonLimpiar = crearBoton("🧹 Limpiar", "Limpiar todos los campos", e -> limpiarCampos());
        JButton botonGuardar = crearBoton("💾 Guardar", "Guardar melodía en archivo JSON", e -> guardarMelodia());
        JButton botonCargar = crearBoton("📂 Cargar", "Cargar melodía desde archivo JSON", e -> cargarMelodia());
        JButton botonReproducir = crearBoton("▶️ Reproducir", "Reproducir la melodía actual", e -> reproducirMelodia());
        JButton botonDetener = crearBoton("⏹️ Detener", "Detener la reproducción", e -> detenerReproduccion());

        panelBotones.add(botonAgregar);
        panelBotones.add(botonModificar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonLimpiar);
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCargar);
        panelBotones.add(botonReproducir);
        panelBotones.add(botonDetener);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, String tooltip, java.awt.event.ActionListener listener) {
        JButton boton = new JButton(texto);
        boton.setToolTipText(tooltip);
        boton.addActionListener(listener);
        return boton;
    }

    private boolean validarNota(String nombreNota) {
        if (nombreNota == null || nombreNota.trim().isEmpty()) {
            return false;
        }
        return NOTAS_VALIDAS.contains(nombreNota.toUpperCase().trim());
    }

    private void agregarNota() {
        try {
            String nombreNota = campoNota.getText().trim();
            Figura figura = (Figura) comboFigura.getSelectedItem();
            int octava = (Integer) spinnerOctava.getValue();

            if (nombreNota.isEmpty()) {
                mostrarMensajeError("Debe ingresar el nombre de la nota.");
                return;
            }

            if (!validarNota(nombreNota)) {
                mostrarMensajeError("Nota inválida.\nLas notas válidas son: DO, RE, MI, FA, SOL, LA, SI\n(o sus equivalencias: C, D, E, F, G, A, B)");
                return;
            }

            Nota nuevaNota = new Nota(nombreNota, figura, octava);
            lista.agregar(nuevaNota);
            actualizarLista();
            limpiarCampos();
            mostrarMensajeInformacion("✅ Nota agregada correctamente.");
        } catch (Exception ex) {
            mostrarMensajeError("Error al agregar nota: " + ex.getMessage());
        }
    }

    private void modificarNota() {
        if (indiceSeleccionado < 0) {
            mostrarMensajeAdvertencia("Seleccione una nota de la lista para modificar.");
            return;
        }

        try {
            String nombreNota = campoNota.getText().trim();
            Figura figura = (Figura) comboFigura.getSelectedItem();
            int octava = (Integer) spinnerOctava.getValue();

            if (nombreNota.isEmpty()) {
                mostrarMensajeError("Debe ingresar el nombre de la nota.");
                return;
            }

            if (!validarNota(nombreNota)) {
                mostrarMensajeError("Nota inválida.\nLas notas válidas son: DO, RE, MI, FA, SOL, LA, SI");
                return;
            }

            Nota notaModificada = new Nota(nombreNota, figura, octava);
            lista.modificar(indiceSeleccionado, notaModificada);
            actualizarLista();
            limpiarCampos();
            mostrarMensajeInformacion("✅ Nota modificada correctamente.");
        } catch (Exception ex) {
            mostrarMensajeError("Error al modificar nota: " + ex.getMessage());
        }
    }

    private void eliminarNota() {
        if (indiceSeleccionado < 0) {
            mostrarMensajeAdvertencia("Seleccione una nota de la lista para eliminar.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea eliminar la nota seleccionada?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            lista.eliminar(indiceSeleccionado);
            actualizarLista();
            limpiarCampos();
            mostrarMensajeInformacion("✅ Nota eliminada correctamente.");
        }
    }

    private void cargarNotaEnCampos(int posicion) {
        Nota nota = lista.obtener(posicion);
        if (nota != null) {
            campoNota.setText(nota.getNombreNota());
            comboFigura.setSelectedItem(nota.getFigura());
            spinnerOctava.setValue(nota.getOctava());
        }
    }

    private void guardarMelodia() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar melodía");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int resultado = fileChooser.showSaveDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".json")) {
                ruta += ".json";
            }
            
            if (gestorJSON.guardar(lista, ruta)) {
                mostrarMensajeInformacion("✅ Melodía guardada exitosamente en:\n" + ruta);
            } else {
                mostrarMensajeError("Error al guardar la melodía. Asegúrese de que tenga notas.");
            }
        }
    }

    private void cargarMelodia() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar melodía");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            
            ListaLigada listaCargada = gestorJSON.cargar(ruta);
            
            if (listaCargada != null) {
                lista = listaCargada;
                modeloLista.clear();
                actualizarLista();
                indiceSeleccionado = -1;
                mostrarMensajeInformacion("✅ Melodía cargada correctamente.");
            } else {
                mostrarMensajeError("Error al cargar la melodía. Verifique el formato del archivo.");
            }
        }
    }

    private void reproducirMelodia() {
        if (lista.isEmpty()) {
            mostrarMensajeAdvertencia("No hay notas para reproducir.\nAgregue notas primero.");
            return;
        }
        
        if (reproductor.reproducir(lista)) {
            System.out.println("▶️ Reproduciendo melodía...");
        } else {
            mostrarMensajeError("Error al reproducir la melodía.\nVerifique que el dispositivo MIDI esté disponible.");
        }
    }

    private void detenerReproduccion() {
        reproductor.detener();
    }

    private void actualizarLista() {
        modeloLista.clear();
        String[] notas = lista.mostrar();
        for (String nota : notas) {
            modeloLista.addElement(nota);
        }
    }

    private void limpiarCampos() {
        campoNota.setText("");
        comboFigura.setSelectedIndex(0);
        spinnerOctava.setValue(4);
        listaNotas.clearSelection();
        indiceSeleccionado = -1;
    }

    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "❌ Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensajeInformacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "✅ Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "⚠️ Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void dispose() {
        reproductor.cerrar();
        super.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new InterfazEditor().setVisible(true));
    }
}