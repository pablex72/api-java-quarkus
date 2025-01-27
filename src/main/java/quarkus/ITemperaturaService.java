package quarkus;

import java.util.List;
import java.util.Optional;

public interface ITemperaturaService {
    void addTemperatura(Temperatura t);

    List<Temperatura> obtenerTemperaturas();
    boolean isEmpty();

    int maxima();

    Optional<Temperatura> sacarTemperatura(String ciudad);
    }
