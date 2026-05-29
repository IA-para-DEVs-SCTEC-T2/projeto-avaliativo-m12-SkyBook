package com.ia.para.devs.skybook.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ia.para.devs.skybook.model.AirplaneSeatEntity;
import com.ia.para.devs.skybook.repository.AirplaneSeatRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Componente responsável por popular o banco de dados com as poltronas da aeronave
 * na inicialização da aplicação.
 *
 * <p>Distribui 60 poltronas em 10 fileiras de 6 assentos cada, organizadas em três classes:</p>
 * <ul>
 *   <li>Fileiras 1–2: Executiva (R$ 198,89)</li>
 *   <li>Fileiras 3–4: Econômica Premium (R$ 149,90)</li>
 *   <li>Fileiras 5–10: Econômica (R$ 110,00)</li>
 * </ul>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AirplaneSeatDataLoader implements CommandLineRunner {

    private static final int TOTAL_ROWS = 10;
    private static final char[] SEAT_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F'};

    private static final BigDecimal PRICE_EXECUTIVE         = new BigDecimal("198.89");
    private static final BigDecimal PRICE_PREMIUM_ECONOMY   = new BigDecimal("149.90");
    private static final BigDecimal PRICE_ECONOMY           = new BigDecimal("110.00");

    private final AirplaneSeatRepository airplaneSeatRepository;

    /**
     * Executa a carga inicial de poltronas caso o banco ainda esteja vazio.
     * Garante idempotência: não insere duplicatas em reinicializações.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    @Override
    public void run(String... args) {
        if (airplaneSeatRepository.count() > 0) {
            log.info("Poltronas já cadastradas. Carga inicial ignorada.");
            return;
        }

        List<AirplaneSeatEntity> seats = new ArrayList<>();

        for (int row = 1; row <= TOTAL_ROWS; row++) {
            BigDecimal price = resolvePrice(row);

            for (char letter : SEAT_LETTERS) {
                AirplaneSeatEntity seat = new AirplaneSeatEntity();
                seat.setCode(row + String.valueOf(letter));
                seat.setPrice(price);
                seat.setAvailable(true);
                seats.add(seat);
            }
        }

        airplaneSeatRepository.saveAll(seats);
        log.info("{} poltronas inseridas com sucesso.", seats.size());
    }

    /**
     * Resolve o preço da poltrona com base no número da fileira.
     *
     * @param row número da fileira (1 a 10)
     * @return preço correspondente à classe da fileira
     */
    private BigDecimal resolvePrice(int row) {
        if (row <= 2) {
            return PRICE_EXECUTIVE;
        } else if (row <= 4) {
            return PRICE_PREMIUM_ECONOMY;
        } else {
            return PRICE_ECONOMY;
        }
    }
}
