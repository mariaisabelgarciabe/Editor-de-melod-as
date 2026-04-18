import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.MidiSystem;

public class ReproductorMidi {
    private static final int CANAL = 0;
    private static final int INSTRUMENTO = 0;
    private static final int BPM = 120;
    private static final int RESOLUCION = 480;

    private Sequencer secuenciador;
    private Sequence secuencia;
    private Track pista;

    public ReproductorMidi() {
        try {
            secuenciador = MidiSystem.getSequencer();
            if (secuenciador != null) {
                secuenciador.open();
                secuencia = new Sequence(Sequence.PPQ, RESOLUCION);
                pista = secuencia.createTrack();
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar MIDI: " + e.getMessage());
            secuenciador = null;
        }
    }

    public boolean reproducir(ListaLigada lista) {
        if (lista.isEmpty() || secuenciador == null) {
            return false;
        }

        try {
            if (!secuenciador.isOpen()) {
                secuenciador.open();
            }

            secuencia = new Sequence(Sequence.PPQ, RESOLUCION);
            pista = secuencia.createTrack();

            ShortMessage programaMessage = new ShortMessage();
            programaMessage.setMessage(ShortMessage.PROGRAM_CHANGE, CANAL, INSTRUMENTO, 0);
            MidiEvent eventoPrograma = new MidiEvent(programaMessage, 0);
            pista.add(eventoPrograma);

            long tiempo = 0;
            double ticksPorRedonda = RESOLUCION * 4;

            for (int i = 0; i < lista.getTamanho(); i++) {
                Nota nota = lista.obtener(i);
                int valorMidi = convertirAMidi(nota.getNombreNota(), nota.getOctava());
                double duracionFigura = nota.getFigura().getDuracion();
                int ticks = (int) (ticksPorRedonda * duracionFigura / 4);

                ShortMessage onMessage = new ShortMessage();
                onMessage.setMessage(ShortMessage.NOTE_ON, CANAL, valorMidi, 100);
                MidiEvent eventoNotaOn = new MidiEvent(onMessage, tiempo);
                pista.add(eventoNotaOn);

                ShortMessage offMessage = new ShortMessage();
                offMessage.setMessage(ShortMessage.NOTE_OFF, CANAL, valorMidi, 0);
                MidiEvent eventoNotaOff = new MidiEvent(offMessage, tiempo + ticks);
                pista.add(eventoNotaOff);

                tiempo += ticks;
            }

            secuenciador.setSequence(secuencia);
            secuenciador.start();

            return true;
        } catch (Exception e) {
            System.err.println("Error al reproducir: " + e.getMessage());
            return false;
        }
    }

    public void detener() {
        if (secuenciador != null && secuenciador.isRunning()) {
            secuenciador.stop();
        }
    }

    public void cerrar() {
        detener();
        if (secuenciador != null && secuenciador.isOpen()) {
            try {
                secuenciador.close();
            } catch (Exception e) {
                System.err.println("Error al cerrar secuenciador: " + e.getMessage());
            }
        }
    }

    private int convertirAMidi(String nombreNota, int octava) {
        int notaBase = obtenerNotaBase(nombreNota);
        
        if (notaBase == -1) {
            return 60;
        }

        return notaBase + (octava * 12);
    }

    private int obtenerNotaBase(String nombreNota) {
        if (nombreNota == null) {
            return -1;
        }
        nombreNota = nombreNota.toUpperCase().trim();
        
        switch (nombreNota) {
            case "DO":
            case "C":
                return 0;
            case "DO#":
            case "REB":
            case "C#":
            case "DB":
                return 1;
            case "RE":
            case "D":
                return 2;
            case "RE#":
            case "MIB":
            case "D#":
            case "EB":
                return 3;
            case "MI":
            case "E":
                return 4;
            case "FA":
            case "F":
                return 5;
            case "FA#":
            case "SOLB":
            case "F#":
            case "GB":
                return 6;
            case "SOL":
            case "G":
                return 7;
            case "SOL#":
            case "LAB":
            case "G#":
            case "AB":
                return 8;
            case "LA":
            case "A":
                return 9;
            case "LA#":
            case "SIB":
            case "A#":
            case "BB":
                return 10;
            case "SI":
            case "B":
                return 11;
            default:
                return -1;
        }
    }
}