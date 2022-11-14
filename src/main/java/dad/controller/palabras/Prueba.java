package dad.controller.palabras;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Prueba {

	public static void main(String[] args) throws Exception {

		Prueba p = new Prueba();
		//System.out.println(p.cargarFichero().toString());
		p.cargar();
		
	}

	public List<Puntos> cargarFichero() throws URISyntaxException, IOException {

		Path scoreFile = Paths.get("puntos.txt");
		List<String> score = Files.readAllLines(scoreFile, StandardCharsets.UTF_8);
		System.out.println(score);
		return score.stream()
				.map(line -> line.split(" - "))
				.map(parts -> {
					String nombre = parts[0];
					int puntos = Integer.parseInt(parts[1].trim());
					return new Puntos(nombre, puntos);
				}).collect(Collectors.toList());

	}

	public void cargar() throws IOException, URISyntaxException {
		//Esto funciona cuando esta dentro de la carpeta resources
	//	Path scoreFile = Paths.get(getClass().getResource("/datos/prueba.txt").toURI());
		Path scoreFile = Paths.get("puntos.txt");

		List<String> score = Files.readAllLines(scoreFile, StandardCharsets.UTF_8);
		System.out.println(score);
	}

}

class Puntos implements Comparable<Puntos> {

	private int puntos;
	private String nombre;

	public Puntos(String nombre, int puntos) {
		this.nombre = nombre;
		this.puntos = puntos;
	}

	public int getPuntos() {
		return puntos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String toString() {
		String text = "El usuario " + nombre + " ha conseguido " + puntos;
		return text;
	}

	@Override
	public int compareTo(Puntos o) {
		int value = 0;
		if (this.getPuntos() > o.getPuntos()) {
			value = 1;
		} else if (this.getPuntos() < o.getPuntos()) {
			value = -1;
		}
		return value;
	}

}
